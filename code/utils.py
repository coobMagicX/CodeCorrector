# -*- coding: utf-8 -*-
"""
Created on 22/6/2023 下午6:51

@author: Cai Muxin
@email: 1607494203@qq.com
"""
import json
import os
import re
import getpass
import signal
import subprocess
import time
import glob
from difflib import unified_diff
from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.runnables.history import RunnableWithMessageHistory
import javalang
from langchain_core.tools import tool
from langchain.agents import AgentExecutor, create_tool_calling_agent
from langchain_core.prompts import ChatPromptTemplate
from langchain.chains.llm import LLMChain
from langchain.chains.sequential import SimpleSequentialChain, SequentialChain
from langchain_openai import ChatOpenAI
from openai import OpenAI
from langchain.globals import set_verbose
from langchain_core.output_parsers import StrOutputParser
from pprint import pprint
import networkx as nx
import matplotlib.pyplot as plt

res_dict = {}

# Referenced and modified from the open-source code of paper Automated Program Repair in the Era of Large Pre-trained Language Models.
def parse_d4j(folder):
    with open(folder + "Defects4j/single_function_repair.json", "r") as f:
        result = json.load(f)
    cleaned_result = {}
    for k, v in result.items():
        lines = v['buggy'].splitlines()
        leading_white_space = len(lines[0]) - len(lines[0].lstrip())
        cleaned_result[k + ".java"] = {"buggy": "\n".join([line[leading_white_space:] for line in lines])}
        lines = v['fix'].splitlines()
        leading_white_space = len(lines[0]) - len(lines[0].lstrip())
        cleaned_result[k + ".java"]["fix"] = "\n".join([line[leading_white_space:] for line in lines])
    return cleaned_result

def load_data(filepath):
    with open(filepath, 'r', encoding='utf-8') as file:
        return json.load(file)

def save_to_json(data, filepath):
    with open(filepath, 'w', encoding='utf-8') as file:
        json.dump(data, file, ensure_ascii=False, indent=4)

def get_unified_diff(source, mutant):
    output = ""
    for line in unified_diff(source.split('\n'), mutant.split('\n'), lineterm=''):
        output += line + "\n"
    return output

may_pass = []
def run_d4j_test(source, testmethods, bug_id):
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
        log = "Syntax Error"
        return compile_fail, timed_out, bugg, entire_bugg, True, log, True

    for t in testmethods:
        # print(t.strip())
        cmd = 'defects4j test -w %s/ -t %s' % (('/tmp/' + bug_id), t.strip())
        print("cmd: \n", cmd)
        Returncode = ""
        error_return = ""
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
                for line in r:
                    if re.search(':\serror:\s', line.decode('utf-8')):
                        error_string = line.decode('utf-8')
                        break
                print(error_string)
                error_return = error_string
                break
            elif time.time() - while_begin > 15:
                error_file.close()
                os.killpg(os.getpgid(child.pid), signal.SIGTERM)
                timed_out = True
                break
            else:
                time.sleep(0.01)
        log = Returncode
        if len(log) > 0 and log[-1].decode('utf-8') == "Failing tests: 0\n":
            continue
        else:
            bugg = True
            break

    if not bugg:
        may_pass.append(bug_id)
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
                break
            else:
                time.sleep(0.01)
        log = Returncode
        if len(log) > 0 and log[-1].decode('utf-8') == "Failing tests: 0\n":
            print('success')
        else:
            entire_bugg = True

    if error_return != "":
        log = error_return
        return compile_fail, timed_out, bugg, entire_bugg, False, log, True
    return compile_fail, timed_out, bugg, entire_bugg, False, log, False

def _run_validation(bug, patch_file, folder, patch, skip_val=True):
    try:
        path = "./repair_data/repair_test_json/" + folder + "/" + patch_file
        print("--try: ", path)
        with open(path, 'w') as f:
            f.write(patch)
    except:
        print("--except: ", path)
        with open(path, 'w') as f:
            f.write("write error ... ")
        return False

    if skip_val:
        print("Skipping validation ... ")
        return False


@tool
def validate_patche(input: str) -> str:
    """Run script test and return the result of test"""
    print("validate_patche_input: \n", input)
    if not input.endswith('.java'):
        print("You have already performed the test")
        return "You have already performed the test"
    patch_path = "path to patch"
    j_file = "lm_repair.json"
    loc_folder = "path to /Defects4j/location"
    with open("path to Defects4j" + "/single_function_repair.json", "r") as f:
        bug_dict = json.load(f)

    with open(patch_path + "/" + j_file, "r") as f:
        codex_dict = json.load(f)

    plausible = 0
    total = 0

    file_list = glob.glob(patch_path + '/*.java')
    for file in sorted(file_list):
        if file.split('/')[-1].split("_")[0]+'.java' == input:
            print("pipei")
            break
        else:
            continue

    if file.split('/')[-1].split("_")[0]+'.java' != input:
        return "No correct patch was generated", False

    file = file.replace("\\", "/")
    current_file = file.split('/')[-1].split("_")[0]


    # print("codex_dict: ", codex_dict)
    if ".java" not in current_file:
        print("no")
        current_file = current_file + ".java"
        index = int(file.split('/')[-1].split("_")[1].split(".")[0]) - 1
    else:
        index = 0

    # 新增
    if codex_dict[current_file] == []:
        print("codex_dict[current_file] :", codex_dict[current_file])
        return "No correct patch was generated", False
    if codex_dict[current_file][index]["finish_reason"] != "stop":
        print("codex_dict[current_file] :", codex_dict[current_file])
        return "No correct patch was generated", False
    if codex_dict[current_file][index]['diff'] == "":
        print("codex_dict[current_file] :", codex_dict[current_file])
        return "No correct patch was generated", False
    bug_id = current_file.split('.')[0]
    project = bug_id.split("-")[0]
    bug = bug_id.split("-")[1]
    start = bug_dict[bug_id]['start']
    end = bug_dict[bug_id]['end']
    tmp_bug_id = "test_" + bug_id

    print("file, bug_id: ", file, bug_id)

    subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
    subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)), shell=True)
    testmethods = os.popen('defects4j export -w %s -p tests.trigger' % ('/tmp/' + tmp_bug_id)).readlines()
    source_dir = os.popen("defects4j export -p dir.src.classes -w /tmp/" + tmp_bug_id).readlines()[-1].strip()

    with open(loc_folder + "/{}.buggy.lines".format(bug_id), "r") as f:
        locs = f.read()

    loc = set([x.split("#")[0] for x in locs.splitlines()])  # should only be one
    loc = loc.pop()
    try:
        with open(file, 'r') as f:
            patch = f.readlines()
    except:
        return "No correct patch was generated"

    try:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r') as f:
            source = f.readlines()
    except:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r', encoding='ISO-8859-1') as f:
            source = f.readlines()


    source = "".join(source[:start - 1] + patch + source[end:])

    backup_file = "./repair_data/" + "/source_check.java"
    try:
        with open(backup_file, 'w') as f:
            f.write(source)
    except:
        with open(backup_file, 'w', encoding='ISO-8859-1') as f:
            f.write(source)

    try:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w') as f:
            f.write(source)
    except:
        with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'w', encoding='ISO-8859-1') as f:
            f.write(source)

    compile_fail, timed_out, bugg, entire_bugg, syntax_error, log, javac_error = run_d4j_test(source, testmethods, tmp_bug_id)
    total += 1
    if not compile_fail and not timed_out and not bugg and not entire_bugg and not syntax_error:
        plausible += 1
        codex_dict[current_file][index]['valid'] = True
        print("{} has valid patch: {}".format(bug_id, file))
        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        return "success", False
    else:
        print("{} has invalid patch: {}".format(bug_id, file))
        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        return log, javac_error

def save_results(data_name, buggy, code_match, well):
    repair_result = []
    p_diff = {}
    outputs = [code_match]
    folder = "test_generation_final"
    t_chances = 1
    while t_chances > 0:
        t_chances -= 10
        if well:
            for index, output in enumerate(outputs):
                diff = get_unified_diff(buggy, output)
                if diff in p_diff:
                    repair_result[p_diff[diff]]['num'] += 1
                    continue
                p_diff[diff] = len(repair_result)
                print("--diff:  ", diff)
                repair_result.append({'output': output,
                                      'diff': diff,
                                      'finish_reason': 'stop',
                                      'valid': _run_validation(data_name.split(".")[0],
                                                               data_name.split(".")[0] + "_" + str(
                                                                   len(repair_result)) + "." + data_name.split(".")[1],
                                                               folder, output, skip_val=True),
                                      'num': 1})

    res_dict[data_name] = repair_result

    with open("path to patch" + "/lm_repair.json","w") as f:  # write to file
        json.dump(res_dict, f)
    print("Exiting loop")

def get_method(buggy):
    pattern = r'\b(\w+)\s*\([^)]*\)'

    matches = re.search(pattern, buggy)
    if matches:
        match = matches.group(1)
        return match
    else:
        return 'No method names found'