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
import chardet
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
from repair_utils import parse_d4j, load_data, save_to_json, get_unified_diff, save_results, get_method


file_list = ['Chart-1', 'Chart-10', 'Chart-11', 'Chart-12', 'Chart-13', 'Chart-17', 'Chart-20', 'Chart-24', 'Chart-26', 'Chart-3', 'Chart-4', 'Chart-5', 'Chart-6', 'Chart-7', 'Chart-8', 'Chart-9',
        'Closure-1', 'Closure-10', 'Closure-101', 'Closure-102', 'Closure-104', 'Closure-105', 'Closure-107', 'Closure-109', 'Closure-11', 'Closure-111', 'Closure-112', 'Closure-113', 'Closure-114', 'Closure-115', 'Closure-116', 'Closure-117', 'Closure-118', 'Closure-119', 'Closure-12', 'Closure-120', 'Closure-121', 'Closure-122', 'Closure-123', 'Closure-124', 'Closure-125', 'Closure-126', 'Closure-128', 'Closure-129', 'Closure-13', 'Closure-130', 'Closure-131', 'Closure-132', 'Closure-133', 'Closure-14', 'Closure-15', 'Closure-17', 'Closure-18', 'Closure-19', 'Closure-2', 'Closure-20', 'Closure-21', 'Closure-22', 'Closure-23', 'Closure-24', 'Closure-25', 'Closure-29', 'Closure-31', 'Closure-32', 'Closure-33', 'Closure-35', 'Closure-36', 'Closure-38', 'Closure-39', 'Closure-4', 'Closure-40', 'Closure-42', 'Closure-44', 'Closure-48', 'Closure-5', 'Closure-50', 'Closure-51', 'Closure-52', 'Closure-53', 'Closure-55', 'Closure-56', 'Closure-57', 'Closure-58', 'Closure-59', 'Closure-61', 'Closure-62', 'Closure-65', 'Closure-66', 'Closure-67', 'Closure-69', 'Closure-7', 'Closure-70', 'Closure-71', 'Closure-73', 'Closure-77', 'Closure-78', 'Closure-81', 'Closure-82', 'Closure-83', 'Closure-86', 'Closure-87', 'Closure-88', 'Closure-91', 'Closure-92', 'Closure-94', 'Closure-95', 'Closure-96', 'Closure-97', 'Closure-99',
        'Lang-1', 'Lang-10', 'Lang-11', 'Lang-12', 'Lang-14', 'Lang-16', 'Lang-17', 'Lang-18', 'Lang-19', 'Lang-21', 'Lang-22', 'Lang-24', 'Lang-26', 'Lang-27', 'Lang-28', 'Lang-29', 'Lang-3', 'Lang-31', 'Lang-33', 'Lang-37', 'Lang-38', 'Lang-39', 'Lang-40', 'Lang-42', 'Lang-43', 'Lang-44', 'Lang-45', 'Lang-48', 'Lang-49', 'Lang-5', 'Lang-51', 'Lang-52', 'Lang-53', 'Lang-54', 'Lang-55', 'Lang-57', 'Lang-58', 'Lang-59', 'Lang-6', 'Lang-61', 'Lang-65', 'Lang-9',
        'Math-10', 'Math-101', 'Math-102', 'Math-103', 'Math-105', 'Math-106', 'Math-11', 'Math-13', 'Math-17', 'Math-19', 'Math-2', 'Math-20', 'Math-21', 'Math-23', 'Math-24', 'Math-25', 'Math-26', 'Math-27', 'Math-28', 'Math-3', 'Math-30', 'Math-31', 'Math-32', 'Math-33', 'Math-34', 'Math-38', 'Math-39', 'Math-40', 'Math-41', 'Math-42', 'Math-43', 'Math-44', 'Math-45', 'Math-48', 'Math-5', 'Math-50', 'Math-51', 'Math-52', 'Math-53', 'Math-55', 'Math-56', 'Math-57', 'Math-58', 'Math-59', 'Math-60', 'Math-63', 'Math-64', 'Math-69', 'Math-7', 'Math-70', 'Math-72', 'Math-73', 'Math-74', 'Math-75', 'Math-78', 'Math-79', 'Math-8', 'Math-80', 'Math-82', 'Math-84', 'Math-85', 'Math-86', 'Math-87', 'Math-88', 'Math-89', 'Math-9', 'Math-90', 'Math-91', 'Math-94', 'Math-95', 'Math-96', 'Math-97',
        'Mockito-1', 'Mockito-12', 'Mockito-13', 'Mockito-18', 'Mockito-20', 'Mockito-22', 'Mockito-24', 'Mockito-27', 'Mockito-28', 'Mockito-29', 'Mockito-33', 'Mockito-34', 'Mockito-38', 'Mockito-5', 'Mockito-7', 'Mockito-8',
        'Time-14', 'Time-15', 'Time-16', 'Time-17', 'Time-18', 'Time-19', 'Time-20', 'Time-22', 'Time-23', 'Time-24', 'Time-25', 'Time-27', 'Time-4', 'Time-5', 'Time-7', 'Time-8'
        ]
file_list = ['Cli-11', 'Cli-12', 'Cli-14', 'Cli-15', 'Cli-17', 'Cli-19', 'Cli-20', 'Cli-23', 'Cli-24', 'Cli-25', 'Cli-26', 'Cli-27', 'Cli-28', 'Cli-29', 'Cli-32', 'Cli-35', 'Cli-37', 'Cli-38', 'Cli-4', 'Cli-40', 'Cli-5', 'Cli-8', 'Cli-9',
        'Codec-10', 'Codec-15', 'Codec-17', 'Codec-18', 'Codec-2', 'Codec-3', 'Codec-4', 'Codec-5', 'Codec-6', 'Codec-7', 'Codec-9',
        'Collections-26',
        'Compress-1', 'Compress-10', 'Compress-11', 'Compress-12', 'Compress-13', 'Compress-14', 'Compress-15', 'Compress-16', 'Compress-17', 'Compress-18', 'Compress-19', 'Compress-21', 'Compress-23', 'Compress-24', 'Compress-25', 'Compress-26', 'Compress-27', 'Compress-28', 'Compress-30', 'Compress-31', 'Compress-32', 'Compress-35', 'Compress-36', 'Compress-37', 'Compress-38', 'Compress-40', 'Compress-41', 'Compress-44', 'Compress-45', 'Compress-46', 'Compress-5', 'Compress-7', 'Compress-8',
        'Csv-1', 'Csv-10', 'Csv-11', 'Csv-14', 'Csv-15', 'Csv-2', 'Csv-3', 'Csv-4', 'Csv-5', 'Csv-6', 'Csv-9',
        'Gson-11', 'Gson-12', 'Gson-13', 'Gson-15', 'Gson-16', 'Gson-17', 'Gson-18', 'Gson-5', 'Gson-6',
        'JacksonCore-11', 'JacksonCore-15', 'JacksonCore-20', 'JacksonCore-21', 'JacksonCore-23', 'JacksonCore-25', 'JacksonCore-26', 'JacksonCore-3', 'JacksonCore-4', 'JacksonCore-5', 'JacksonCore-6', 'JacksonCore-7', 'JacksonCore-8',
        'JacksonDatabind-1', 'JacksonDatabind-100', 'JacksonDatabind-101', 'JacksonDatabind-102', 'JacksonDatabind-107', 'JacksonDatabind-11', 'JacksonDatabind-112', 'JacksonDatabind-12', 'JacksonDatabind-16', 'JacksonDatabind-17', 'JacksonDatabind-19', 'JacksonDatabind-24', 'JacksonDatabind-27', 'JacksonDatabind-28', 'JacksonDatabind-33', 'JacksonDatabind-34', 'JacksonDatabind-35', 'JacksonDatabind-37', 'JacksonDatabind-39', 'JacksonDatabind-42', 'JacksonDatabind-44', 'JacksonDatabind-45', 'JacksonDatabind-46', 'JacksonDatabind-47', 'JacksonDatabind-49', 'JacksonDatabind-5', 'JacksonDatabind-51', 'JacksonDatabind-54', 'JacksonDatabind-57', 'JacksonDatabind-58', 'JacksonDatabind-6', 'JacksonDatabind-62', 'JacksonDatabind-64', 'JacksonDatabind-67', 'JacksonDatabind-7', 'JacksonDatabind-70', 'JacksonDatabind-71', 'JacksonDatabind-74', 'JacksonDatabind-76', 'JacksonDatabind-8', 'JacksonDatabind-82', 'JacksonDatabind-83', 'JacksonDatabind-85', 'JacksonDatabind-88', 'JacksonDatabind-9', 'JacksonDatabind-91', 'JacksonDatabind-93', 'JacksonDatabind-96', 'JacksonDatabind-97', 'JacksonDatabind-98', 'JacksonDatabind-99',
        'JacksonXml-1', 'JacksonXml-3', 'JacksonXml-4', 'JacksonXml-5',
        'Jsoup-1', 'Jsoup-10', 'Jsoup-13', 'Jsoup-15', 'Jsoup-19', 'Jsoup-2', 'Jsoup-20', 'Jsoup-24', 'Jsoup-26', 'Jsoup-27', 'Jsoup-32', 'Jsoup-33', 'Jsoup-34', 'Jsoup-35', 'Jsoup-37', 'Jsoup-38', 'Jsoup-39', 'Jsoup-40', 'Jsoup-41', 'Jsoup-42', 'Jsoup-43', 'Jsoup-45', 'Jsoup-46', 'Jsoup-47', 'Jsoup-48', 'Jsoup-49', 'Jsoup-5', 'Jsoup-50', 'Jsoup-51', 'Jsoup-53', 'Jsoup-54', 'Jsoup-55', 'Jsoup-57', 'Jsoup-59', 'Jsoup-6', 'Jsoup-61', 'Jsoup-62', 'Jsoup-64', 'Jsoup-68', 'Jsoup-70', 'Jsoup-72', 'Jsoup-75', 'Jsoup-76', 'Jsoup-77', 'Jsoup-80', 'Jsoup-82', 'Jsoup-84', 'Jsoup-85', 'Jsoup-86', 'Jsoup-88', 'Jsoup-89', 'Jsoup-90', 'Jsoup-93',
        'JxPath-10', 'JxPath-12', 'JxPath-21', 'JxPath-22', 'JxPath-5', 'JxPath-6', 'JxPath-8',
        'Closure-145', 'Closure-146', 'Closure-150', 'Closure-152', 'Closure-159', 'Closure-160', 'Closure-161', 'Closure-164', 'Closure-166', 'Closure-168', 'Closure-172', 'Closure-176'
        ]


openai_api_key = 'your key'

def extract_ast(source_code):
    try:
        tokens = javalang.tokenizer.tokenize(source_code)
        parser = javalang.parser.Parser(tokens)
        return parser.parse()
    except javalang.parser.JavaSyntaxError as e:
        print("Syntax error while parsing Java code:", e)
    except Exception as e:
        print("An error occurred while parsing Java code:", e)
    return None

def find_method_node(node, method_name):
    if isinstance(node, javalang.tree.CompilationUnit):
        for type_decl in node.types:
            method_node = find_method_node(type_decl, method_name)
            if method_node:
                return method_node
    elif isinstance(node, javalang.tree.ClassDeclaration):
        for member in node.body:
            method_node = find_method_node(member, method_name)
            if method_node:
                return method_node
    elif isinstance(node, javalang.tree.MethodDeclaration):
        if node.name == method_name:
            node_list.append(node)
    return None

def find_class_node(node, class_name):
    if isinstance(node, javalang.tree.CompilationUnit):
        for type_decl in node.types:
            if isinstance(type_decl, javalang.tree.ClassDeclaration) and type_decl.name == class_name:
                return type_decl
            class_node = find_class_node(type_decl, class_name)
            if class_node:
                return class_node
    elif isinstance(node, javalang.tree.ClassDeclaration):
        for member in node.body:
            if isinstance(member, javalang.tree.ClassDeclaration) and member.name == class_name:
                return member
            class_node = find_class_node(member, class_name)
            if class_node:
                return class_node
    return None

def find_constructor_or_method(node, name):
    if isinstance(node, javalang.tree.ClassDeclaration):
        for member in node.body:
            if isinstance(member, (javalang.tree.MethodDeclaration, javalang.tree.ConstructorDeclaration)) and member.name == name:
                member_list.append(member)
    return None

def find_node_in_ast(ast, name):
    class_node = find_class_node(ast, name)
    if class_node:
        print("find class:", name)
    else:
        print("no class")
    
    find_method_node(ast, name)
    print("node_list: \n", len(node_list))
    
    if class_node:
        find_constructor_or_method(class_node, name)
        print("member_list: \n", len(member_list))

def extract_method_code(source_code, method_node):
    lines = source_code.splitlines()
    start_line = method_node.position.line - 1

    open_braces_count = 0
    found_first_brace = False
    end_line = start_line

    while end_line < len(lines):
        line = lines[end_line]
        open_braces_count += line.count('{')
        open_braces_count -= line.count('}')

        if '{' in line:
            found_first_brace = True
        if found_first_brace and open_braces_count == 0:
            break

        end_line += 1

    method_code = "\n".join(lines[start_line:end_line + 1])
    return method_code


def detect_encoding(file_path):
    with open(file_path, 'rb') as f:
        result = chardet.detect(f.read())
        return result['encoding']


if __name__ == '__main__':

    datasets = parse_d4j(folder="./repair_data/")
    testcase_dict = {}
    for idx, (data_name, dataset) in enumerate(datasets.items()):
        if data_name.split('.')[0] not in file_list:
            continue
        buggy = dataset['buggy']
        # print('buggy: ', buggy)

        # print("-----------------------  Check Out  -----------------------")
        with open("./repair_data/Defects4j" + "/single_function_repair.json", "r") as f:
            bug_dict = json.load(f)

        bug_id = data_name.split('.')[0]
        project = bug_id.split("-")[0]
        bug = bug_id.split("-")[1]
        tmp_bug_id = "test_" + bug_id
        start = bug_dict[bug_id]['start']
        end = bug_dict[bug_id]['end']

        # # print(bug_id)
        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)), shell=True)
        testmethods = os.popen('defects4j export -w %s -p tests.trigger' % ('/tmp/' + tmp_bug_id)).readlines()
        source_dir = os.popen("defects4j export -p dir.src.classes -w /tmp/" + tmp_bug_id).readlines()[-1].strip()
        with open("./repair_data/Defects4j/location" + "/{}.buggy.lines".format(bug_id), "r") as f:
            locs = f.read()

        loc = set([x.split("#")[0] for x in locs.splitlines()])  # should only be one
        loc = loc.pop()

        try:
            with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r') as f:
                source = f.readlines()
        except:
            with open("/tmp/" + tmp_bug_id + "/" + source_dir + "/" + loc, 'r', encoding='ISO-8859-1') as f:
                source = f.readlines()

        # fail_test = []
        # tag = []
        # for t in testmethods:
        #     cmd = 'defects4j test -w %s/ -t %s' % (('/tmp/' + bug_id), t.strip())
        #     Returncode = ""
        #     error_file = open("stderr.txt", "wb")
        #     child = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=error_file, bufsize=-1,
        #                              start_new_session=True)
        #     while_begin = time.time()
        #     while True:
        #         Flag = child.poll()
        #         if Flag == 0:
        #             Returncode = child.stdout.readlines()  # child.stdout.read()
        #             print(b"".join(Returncode).decode('utf-8'))
        #             error_file.close()
        #             break
        #         elif Flag != 0 and Flag is not None:
        #             compile_fail = True
        #             error_file.close()
        #             with open("stderr.txt", "rb") as f:
        #                 r = f.readlines()
        #             for line in r:
        #                 if re.search(':\serror:\s', line.decode('utf-8')):
        #                     error_string = line.decode('utf-8')
        #                     break
        #             break
        #         elif time.time() - while_begin > 15:
        #             error_file.close()
        #             os.killpg(os.getpgid(child.pid), signal.SIGTERM)
        #             timed_out = True
        #             break
        #         else:
        #             time.sleep(0.01)
        #     log = Returncode
        #     if len(log) > 0 and log[-1].decode('utf-8') == "Failing tests: 0\n":
        #         continue
        #     else:
        #         bugg = True
        #         fail_test.append(t)
        # print("fail_test: ", fail_test)
        # if fail_test != []:
        #     testcase_dict[bug_id] = fail_test
        # else:
        #     tag.append(bug_id)

        # print("files in dir: \n", os.listdir("/tmp/" + tmp_bug_id))

        print("-----------------------  Extract TestCase  -----------------------")
        testcase_path = "./repair_data/repair_test_json/FailTestCase/Code/2.0/"
        with open(testcase_path + "fail_test_case.json", "r") as f:
            testcase_dict = json.load(f)
        test_id = 0
        for t in testcase_dict[bug_id]:
            node_list = []
            member_list = []
            method_list = []
            class_list = []
            constructor_list = []

            testcase_split = t.split("::")
            print("testcase_split: ", testcase_split)
            testcase_name = testcase_split[-1]
            if testcase_name.endswith('\n'):
                testcase_name = testcase_name[:-1]
            print("testcase_name: ", testcase_name)

            paths = [
                '/tmp/' + tmp_bug_id + '/tests/' + testcase_split[0].replace(".", "/") + '.java',  # for Chart
                '/tmp/' + tmp_bug_id + '/test/' + testcase_split[0].replace(".", "/") + '.java',   # for Closure Mockito
                '/tmp/' + tmp_bug_id + '/src/test/' + testcase_split[0].replace(".", "/") + '.java',  # for Lang (Path 1)
                '/tmp/' + tmp_bug_id + '/src/test/java/' + testcase_split[0].replace(".", "/") + '.java',  # for Lang (Path 2)
                '/tmp/' + tmp_bug_id + '/gson/src/test/java/' + testcase_split[0].replace(".", "/") + '.java'  # for Gson
            ]

            testcode = None
            for testcode_path in paths:
                try:
                    encoding = detect_encoding(testcode_path)
                    with open(testcode_path, "r", encoding=encoding) as f:
                        testcode = "".join(f.readlines())
                        break
                except FileNotFoundError:
                    continue
                except UnicodeDecodeError as e:
                    print(f"Error decoding file {testcode_path} with detected encoding {encoding}: {e}")
                    raise Exception("wrong")

            print("testcode_path: ", testcode_path)

            testcode_ast = extract_ast(testcode)
            find_node_in_ast(testcode_ast, testcase_name)
            # 判断是否有这个testcase
            if len(node_list) != 0:
                testcase_node = node_list[0]
            else:
                continue
            testcase_code = extract_method_code(testcode, testcase_node)
            print("testcase_code: \n", testcase_code)

            with open(testcase_path + bug_id + "_" + testcase_name + ".java", "w") as f:
                f.write(testcase_code)
            test_id += 1

        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)

        # print("-----------------------  Extract Trace1 -----------------------")
        # subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        # subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)), shell=True)
        # subprocess.run("defects4j compile -w %s" % (('/tmp/' + tmp_bug_id)), shell=True)
        # subprocess.run("defects4j test -w %s" % (('/tmp/' + tmp_bug_id)), shell=True)
        # print("files in dir: \n", os.listdir("/tmp/" + tmp_bug_id))
        # with open("/tmp/" + tmp_bug_id + "/failing_tests", "r") as f:
        #     trace = f.read()

        # trace_path = "./repair_data/repair_test_json/FailTestCase/Trace/2.0/"
        # with open(trace_path + bug_id + ".txt", "w") as f:
        #     f.write(trace)

        # subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        # raise Exception("stop")

        # print("-----------------------  Extract Trace2 -----------------------")
        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        subprocess.run("defects4j checkout -p %s -v %s -w %s" % (project, bug + 'b', ('/tmp/' + tmp_bug_id)), shell=True)

        testcase_path = "path to FailTestCase Code"
        with open(testcase_path + "fail_test_case.json", "r") as f:
            testcase_dict = json.load(f)
        test_id = 0

        trace_path = "path to FailTestCase Trace"
        with open(trace_path + bug_id + ".txt", "r") as f:
            trace_list = f.readlines()

        matching_lines = {}
        matching_codes = {}
        for t in testcase_dict[bug_id]:
            testcase_split = t.split("::")
            print("testcase_split: ", testcase_split)
            testcase_name = testcase_split[-1]
            if testcase_name.endswith('\n'):
                testcase_name = testcase_name[:-1]
            paths = [
                '/tmp/' + tmp_bug_id + '/tests/' + testcase_split[0].replace(".", "/"),  # for Chart
                '/tmp/' + tmp_bug_id + '/test/' + testcase_split[0].replace(".", "/"),   # for Closure Mockito
                '/tmp/' + tmp_bug_id + '/src/test/' + testcase_split[0].replace(".", "/"),  # for Lang
                '/tmp/' + tmp_bug_id + '/src/test/java/' + testcase_split[0].replace(".", "/"),  # for Lang
                '/tmp/' + tmp_bug_id + '/gson/src/test/java/' + testcase_split[0].replace(".", "/")  # for Gson
            ]

            testcode = None

            for path in paths:
                testcode_path = path + '.java'
                if os.path.exists(testcode_path):
                    try:
                        encoding = detect_encoding(testcode_path)
                        with open(testcode_path, "r", encoding=encoding) as f:
                            testcode = f.readlines()
                        break
                    except FileNotFoundError:
                        continue
                    except UnicodeDecodeError as e:
                        print(f"Error decoding file {testcode_path} with detected encoding {encoding}: {e}")
                        raise Exception("wrong")
            print("testcode_path", testcode_path)
            replaced_t = t.replace("::", ".").strip()
            print("replaced_t", replaced_t)
            for tl in trace_list:
                if replaced_t in tl:
                    match = re.search(r'\((.*?)\)', tl)
                    if match:
                        content_inside_parentheses = match.group(1)
                        content = content_inside_parentheses.split(".")[0]
                        line = content_inside_parentheses.split(".")[1].split(":")[1]
                        matching_lines[testcase_name] = line
                        print("content_inside_parentheses: ", content_inside_parentheses)
                        print("testcode:", testcode)
                        tracecode = testcode[int(line)-1]
                        print("tracecode: ", tracecode)
                        matching_codes[testcase_name] = tracecode

        print("len(matching_lines): ", len(matching_lines))
        if len(matching_lines) == 0:
            print(bug_id)
        lines_path = "./repair_data/repair_test_json/FailTestCase/LineLoc/2.0/"
        with open(lines_path + bug_id + ".json", "w") as f:
                json.dump(matching_lines, f)
        code_path = "./repair_data/repair_test_json/FailTestCase/Line/2.0/"
        with open(code_path + bug_id + ".json", "w") as f:
                json.dump(matching_codes, f)

        print("testcode_path: ", testcode_path)
