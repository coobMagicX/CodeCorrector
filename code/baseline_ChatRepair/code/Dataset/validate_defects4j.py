import json
import time
import javalang
import subprocess
import re
import os
import signal
import argparse
from javalang.tokenizer import tokenize
from Dataset.dataset import get_unified_diff


def run_d4j_test(source, testmethods, bug_id, project, bug):
    bugg = False
    compile_fail = False
    timed_out = False
    entire_bugg = False
    error_string = ""

    try:
        tokens = javalang.tokenizer.tokenize(source)
        parser = javalang.parser.Parser(tokens)
        parser.parse()
    except:
        print("Syntax Error")
        return compile_fail, timed_out, bugg, entire_bugg, True, "SyntaxError"

    for t in testmethods:
        cmd = 'defects4j test -w %s/ -t %s' % (('/tmp/' + bug_id), t.strip())
        Returncode = ""
        error_file = open("stderr.txt", "wb")
        child = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=error_file, bufsize=-1,
                                 start_new_session=True)
        while_begin = time.time()
        while True:
            Flag = child.poll()
            if Flag == 0:
                Returncode = child.stdout.readlines()  # child.stdout.read()
                print(b"".join(Returncode).decode('utf-8'))
                error_file.close()
                break
            elif Flag != 0 and Flag is not None:
                compile_fail = True
                error_file.close()
                with open("stderr.txt", "rb") as f:
                    r = f.readlines()
                for index, line in enumerate(r):
                    if re.search(':\serror:\s', line.decode('utf-8')):
                        error_string = line.decode('utf-8').strip()
                        if "cannot find symbol" in error_string:
                            error_string += " (" + r[index + 3].decode('utf-8').split("symbol:")[-1].strip() + ")"
                        break
                print("Error")
                print(error_string)
                if error_string == "":
                    subprocess.run('rm -rf ' + '/tmp/' + bug_id, shell=True)
                    subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + bug_id)),
                                   shell=True)

                break
            elif time.time() - while_begin > 15:
                error_file.close()
                os.killpg(os.getpgid(child.pid), signal.SIGTERM)
                timed_out = True
                error_string = "TimeOutError"
                break
            else:
                time.sleep(0.001)
        log = Returncode
        if len(log) > 0 and log[-1].decode('utf-8') == "Failing tests: 0\n":
            continue
        else:
            bugg = True
            break

    # Then we check if it passes all the tests, include the previously okay tests
    if not bugg:
        print('So you pass the basic tests, Check if it passes all the test, include the previously passing tests')
        cmd = 'defects4j test -w %s/' % ('/tmp/' + bug_id)
        Returncode = ""
        child = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, bufsize=-1,
                                 start_new_session=True)
        while_begin = time.time()
        while True:
            Flag = child.poll()
            if Flag == 0:
                Returncode = child.stdout.readlines()  # child.stdout.read()
                break
            elif Flag != 0 and Flag is not None:
                bugg = True
                break
            elif time.time() - while_begin > 180:
                os.killpg(os.getpgid(child.pid), signal.SIGTERM)
                bugg = True
                error_string = "TimeOutError"
                break
            else:
                time.sleep(0.01)
        log = Returncode
        if len(log) > 0 and log[-1].decode('utf-8') == "Failing tests: 0\n":
            print('success')
        else:
            entire_bugg = True

    return compile_fail, timed_out, bugg, entire_bugg, False, error_string


REAL_SOURCE = []

# 修改=============================================================
def parse_source(source):
    method_dict = {}
    tree = javalang.parse.parse(source)
    for path, node in tree:
        if type(node) == javalang.tree.MethodDeclaration or type(node) == javalang.tree.ConstructorDeclaration:
            print("** node **: \n", node)
            method_dict[node.name] = {'start': node.start_position.line, 'end': node.end_position.line}
    return method_dict

def locate_token_index_for_node(tokens, node):
    """
    根据 javalang AST 节点的 node.position (行,列)，
    在 tokens 中找出最接近该行列的 Token 索引，供后续向后扫描。
    如果 node.position 为 None，返回 None。

    注：此处只是简单地找“第一个 >= (行, 列) 的 token”，
    如果所有 Token 的位置都比 node.position 小，就返回最后一个 Token 的索引。
    """
    if not node.position:
        return None
    node_line, node_col = node.position

    for i, tk in enumerate(tokens):
        t_line, t_col = tk.position
        # 若发现 token 的行号 > node_line, 或行号相等但列号 >= node_col，认为是最接近的
        if (t_line > node_line) or (t_line == node_line and t_col >= node_col):
            return i
    return len(tokens) - 1

def find_method_body_braces(tokens, method_node):
    """
    在 Token 列表中，从 method_node.position 开始向后扫描，
    找到方法体的第一个 '{'（body_start_index）与与之匹配的 '}'（body_end_index）。
    返回 (body_start_index, body_end_index) 均为 Token 在列表中的下标。
    若没找到（可能是抽象方法或接口方法），则返回 (None, None)。
    """
    start_idx = locate_token_index_for_node(tokens, method_node)
    if start_idx is None:
        return None, None

    body_start_index = None
    # 1) 找到第一个 '{'
    for i in range(start_idx, len(tokens)):
        if tokens[i].value == '{':
            body_start_index = i
            break
    if body_start_index is None:
        # 说明这不是一个有方法体的声明 (也可能是接口方法/抽象方法)
        return None, None

    # 2) 用大括号计数，从 body_start_index 开始找到匹配的 '}'
    brace_count = 0
    body_end_index = None
    for j in range(body_start_index, len(tokens)):
        if tokens[j].value == '{':
            brace_count += 1
        elif tokens[j].value == '}':
            brace_count -= 1
            if brace_count == 0:
                body_end_index = j
                break

    return body_start_index, body_end_index

def extract_method_line_ranges(source):
    """
    返回一个字典 { method_name: (start_line, end_line) }，
    其中 start_line 和 end_line 分别是方法体 '{' 和对应的 '}' 的行号。
    如果方法没有方法体(如抽象方法/接口方法)，则返回 None。
    """
    # 解析得到 AST
    tree = javalang.parse.parse(source)
    # 获取全部 Token
    tokens = list(tokenize(source))

    method_dict = {}
    # 遍历 AST 找到所有方法或构造函数声明
    for path, node in tree:
        if isinstance(node, (javalang.tree.MethodDeclaration, javalang.tree.ConstructorDeclaration)):
            # 方法名，构造函数则可以用 node.name 或自定义key
            method_name = node.name

            body_start_index, body_end_index = find_method_body_braces(tokens, node)
            if body_start_index is not None and body_end_index is not None:
                start_line = tokens[body_start_index].position[0]
                end_line = tokens[body_end_index].position[0]
                method_dict[method_name] = {'start': start_line, 'end': end_line}
            else:
                # 对于无方法体的情况，比如抽象方法/接口方法
                # method_dict[method_name] = None
                continue
    return method_dict

# 修改=============================================================

def grab_failing_testcode(bug_id, file_name, test_method_name, line_number, tmp_bug_id):
    test_dir = os.popen("defects4j export -p dir.src.tests -w /tmp/" + tmp_bug_id).readlines()[-1].strip()

    if not os.path.isfile("/tmp/" + tmp_bug_id + "/" + test_dir + "/" + file_name + ".java"):
        return "", ""
    try:
        with open("/tmp/" + tmp_bug_id + "/" + test_dir + "/" + file_name + ".java", "r") as f:
            source = f.read()
    except:
        with open("/tmp/" + tmp_bug_id + "/" + test_dir + "/" + file_name + ".java", "r", encoding='ISO-8859-1') as f:
            source = f.read()
    print("** source **: \n", source)
    # method_dict = parse_source(source)
    method_dict = extract_method_line_ranges(source)
    lines = source.splitlines()

    if line_number == "":
        return "\n".join(lines[method_dict[test_method_name]['start'] - 1:method_dict[test_method_name]['end']]), ""
    else:
        return "\n".join(lines[method_dict[test_method_name]['start'] - 1:method_dict[test_method_name]['end']]), \
               lines[int(line_number) - 1]


def validate_one_patch(folder, patch, bug_id, dataset_name="defects4j_1.2_full", tmp_prefix="test", reset=False):
    global REAL_SOURCE
    bug_id = bug_id.split(".")[0]
    if dataset_name == "defects4j_1.2_full":
        with open(folder + "Defects4j" + "/single_function_repair.json", "r") as f:
            bug_dict = json.load(f)

    bug, project = bug_id.split("-")[1], bug_id.split("-")[0]
    start = bug_dict[bug_id]['start']
    end = bug_dict[bug_id]['end']
    with open(folder + "Defects4j/location" + "/{}.buggy.lines".format(bug_id), "r") as f:
        locs = f.read()
    loc = set([x.split("#")[0] for x in locs.splitlines()])  # should only be one
    loc = loc.pop()
    tmp_bug_id = tmp_prefix + project + bug

    if reset:  # check out project again
        subprocess.run('rm -rf ' + '/tmp/' + tmp_prefix + "*", shell=True)  # clean up
        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)),
                       shell=True)

    # # 补充配置环境
    # subprocess.run("bash -c 'export ANT_HOME=/usr/share/ant && export PATH=$ANT_HOME/bin:$PATH && source ~/.bashrc "
    #                "&& ant -version &&export PATH=$PATH:/home/usersuper/Project/CMX/defects4j/framework/bin && defects4j'",
    #                shell=True)
    testmethods = os.popen('defects4j export -w %s -p tests.trigger' % ('/tmp/' + tmp_bug_id)).readlines()
    source_dir = os.popen("defects4j export -p dir.src.classes -w /tmp/" + tmp_bug_id).readlines()[-1].strip()

    if reset:
        try:
            with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r') as f:
                REAL_SOURCE = f.read().splitlines()
        except:
            with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r', encoding='ISO-8859-1') as f:
                REAL_SOURCE = f.read().splitlines()

    source = REAL_SOURCE
    source = "\n".join(source[:start - 1] + patch.splitlines() + source[end:])

    try:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w') as f:
            f.write(source)
        subprocess.run("touch -d '12 December' " + "/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc,
                       shell=True)
    except:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w', encoding='ISO-8859-1') as f:
            f.write(source)
        subprocess.run("touch -d '12 December' " + "/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc,
                       shell=True)

    compile_fail, timed_out, bugg, entire_bugg, syntax_error, error_string = run_d4j_test(source,
                                                                                          testmethods,
                                                                                          tmp_bug_id,
                                                                                          project, bug)
    if not compile_fail and not timed_out and not bugg and not entire_bugg and not syntax_error:
        print("{} has valid patch".format(bug_id))
        return True, ("valid", "", "")
    else:
        test_method_name, failing_line = "", ""
        if (bugg or entire_bugg) and not compile_fail:
            if not os.path.isfile('/tmp/' + tmp_bug_id + '/failing_tests'):
                error_string = ""
            else:
                try:
                    with open('/tmp/' + tmp_bug_id + '/failing_tests', "r") as f:
                        text = f.read()
                except:
                    with open('/tmp/' + tmp_bug_id + '/failing_tests', "r", encoding='ISO-8859-1') as f:
                        text = f.read()
                if len(text.split("--- ")) >= 2:
                    # error_string = text[1]
                    x = text.split("--- ")[1]  # just grab first one
                    test_name = x.splitlines()[0]
                    file_name = test_name.split("::")[0].replace(".", "/")
                    if len(test_name.split("::")) == 1:
                        error_string = ""
                    else:
                        test_method_name = test_name.split("::")[1]
                        line_number = ""
                        error_string = x.splitlines()[1]
                        for line in x.splitlines()[1:]:
                            if test_method_name in line:
                                line_number = line.split(":")[-1].split(")")[0]
                                file_name = line.split("." + test_method_name)[0].split("at ")[1].replace(".", "/")
                                break
                        print(file_name, test_method_name, line_number)
                        failing_function, failing_line = grab_failing_testcode(bug_id.split(".java")[0], file_name,
                                                                               test_method_name, line_number, tmp_bug_id)
                else:
                    error_string = ""
        print("{} has invalid patch".format(bug_id))
        return False, (error_string, test_method_name, failing_line)


# used to mass validate a bunch of generated files
def validate_all_patches(folder, j_file, dataset_name, tmp_prefix="test"):
    if dataset_name == "defects4j_1.2_full":
        with open("Defects4j" + "/single_function_repair.json", "r") as f:
            bug_dict = json.load(f)

    with open(folder + "/" + j_file, "r") as f:
        repair_dict = json.load(f)

    plausible, total = 0, 0
    seen_bugs = {}

    bugs_with_plausible_patch = []

    for s, patches in repair_dict.items():
        bug_id = s.split(".java")[0]
        bug = bug_id.split("-")[1]
        project = bug_id.split("-")[0]
        start = bug_dict[bug_id]['start']
        end = bug_dict[bug_id]['end']
        with open("Defects4j/location" + "/{}.buggy.lines".format(bug_id), "r") as f:
            locs = f.read()
        loc = set([x.split("#")[0] for x in locs.splitlines()])  # should only be one
        loc = loc.pop()

        tmp_bug_id = tmp_prefix + project + bug

        if tmp_bug_id not in seen_bugs:
            for sb in seen_bugs.keys():
                subprocess.run('rm -rf ' + '/tmp/' + sb, shell=True)
            seen_bugs[tmp_bug_id] = 1
            subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
            subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)),
                           shell=True)
            with open(folder + "/" + j_file, "w") as f:
                json.dump(repair_dict, f)

        testmethods = os.popen('defects4j export -w %s -p tests.trigger' % ('/tmp/' + tmp_bug_id)).readlines()
        source_dir = os.popen("defects4j export -p dir.src.classes -w /tmp/" + tmp_bug_id).readlines()[-1].strip()

        diff_set = set()

        for index, generation in enumerate(patches):
            patch = generation['patch']
            lines = bug_dict[bug_id]['fix'].splitlines()
            leading_white_space = len(lines[0]) - len(lines[0].lstrip())
            fix = "\n".join([line[leading_white_space:] for line in lines])
            diff = get_unified_diff(fix, patch)
            if diff in diff_set:
                continue
            diff_set.add(diff)
            print(diff)

            if seen_bugs[tmp_bug_id] == 1:
                try:
                    with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r') as f:
                        source = f.read().splitlines()
                except:
                    with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r', encoding='ISO-8859-1') as f:
                        source = f.read().splitlines()
                seen_bugs[tmp_bug_id] = source
            else:
                source = seen_bugs[tmp_bug_id]
            source = "\n".join(source[:start - 1] + patch.splitlines() + source[end + 1:])

            try:
                with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w') as f:
                    f.write(source)
                subprocess.run("touch -d '12 December' " + "/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc,
                               shell=True)
            except:
                with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w', encoding='ISO-8859-1') as f:
                    f.write(source)
                subprocess.run("touch -d '12 December' " + "/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc,
                               shell=True)

            compile_fail, timed_out, bugg, entire_bugg, syntax_error, error_string = run_d4j_test(source,
                                                                                                  testmethods,
                                                                                                  tmp_bug_id,
                                                                                                  project, bug)
            if not compile_fail and not timed_out and not bugg and not entire_bugg and not syntax_error:
                plausible += 1
                repair_dict[s][index]['valid'] = True
                print('success')
                print("{} has valid patch: {}".format(bug_id, index))
                bugs_with_plausible_patch.append(bug_id)
            else:
                repair_dict[s][index]['error'] = error_string
                print("{} has invalid patch: {}".format(bug_id, index))

            total += 1

    print("{}/{} are plausible".format(plausible, total))

    with open(folder + "/" + j_file, "w") as f:
        json.dump(repair_dict, f)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--folder", type=str, default="../Generation/Results/test")
    parser.add_argument("--jfile", type=str, default="lm_repair.json")
    parser.add_argument("--dataset_name", type=str, default="defects4j_1.2_full")
    parser.add_argument("--project_name", type=str, default=None)
    parser.add_argument("--bug_id_g", type=str, default=None)
    parser.add_argument("--tmp", type=str, default="test")  # facilitate parallel runs
    args = parser.parse_args()

    validate_all_patches(args.folder, args.jfile, args.dataset_name, tmp_prefix=args.tmp)


if __name__ == "__main__":
    main()
