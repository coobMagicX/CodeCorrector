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
import ast
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
from repair_utils import parse_d4j, load_data, save_to_json, get_unified_diff, save_results, get_method, validate_patche, _run_validation, run_d4j_test



v1_file_list = ['Chart-1', 'Chart-10', 'Chart-11', 'Chart-12', 'Chart-13', 'Chart-17', 'Chart-20', 'Chart-24', 'Chart-26', 'Chart-3', 'Chart-4', 'Chart-5', 'Chart-6', 'Chart-7', 'Chart-8', 'Chart-9',
        'Closure-1', 'Closure-10', 'Closure-101', 'Closure-102', 'Closure-104', 'Closure-105', 'Closure-107', 'Closure-109', 'Closure-11', 'Closure-111', 'Closure-112', 'Closure-113', 'Closure-114', 'Closure-115', 'Closure-116', 'Closure-117', 'Closure-118', 'Closure-119', 'Closure-12', 'Closure-120', 'Closure-121', 'Closure-122', 'Closure-123', 'Closure-124', 'Closure-125', 'Closure-126', 'Closure-128', 'Closure-129', 'Closure-13', 'Closure-130', 'Closure-131', 'Closure-132', 'Closure-133', 'Closure-14', 'Closure-15', 'Closure-17', 'Closure-18', 'Closure-19', 'Closure-2', 'Closure-20', 'Closure-21', 'Closure-22', 'Closure-23', 'Closure-24', 'Closure-25', 'Closure-29', 'Closure-31', 'Closure-32', 'Closure-33', 'Closure-35', 'Closure-36', 'Closure-38', 'Closure-39', 'Closure-4', 'Closure-40', 'Closure-42', 'Closure-44', 'Closure-48', 'Closure-5', 'Closure-50', 'Closure-51', 'Closure-52', 'Closure-53', 'Closure-55', 'Closure-56', 'Closure-57', 'Closure-58', 'Closure-59', 'Closure-61', 'Closure-62', 'Closure-65', 'Closure-66', 'Closure-67', 'Closure-69', 'Closure-7', 'Closure-70', 'Closure-71', 'Closure-73', 'Closure-77', 'Closure-78', 'Closure-81', 'Closure-82', 'Closure-83', 'Closure-86', 'Closure-87', 'Closure-88', 'Closure-91', 'Closure-92', 'Closure-94', 'Closure-95', 'Closure-96', 'Closure-97', 'Closure-99',
        'Lang-1', 'Lang-10', 'Lang-11', 'Lang-12', 'Lang-14', 'Lang-16', 'Lang-17', 'Lang-18', 'Lang-19', 'Lang-21', 'Lang-22', 'Lang-24', 'Lang-26', 'Lang-27', 'Lang-28', 'Lang-29', 'Lang-3', 'Lang-31', 'Lang-33', 'Lang-37', 'Lang-38', 'Lang-39', 'Lang-40', 'Lang-42', 'Lang-43', 'Lang-44', 'Lang-45', 'Lang-48', 'Lang-49', 'Lang-5', 'Lang-51', 'Lang-52', 'Lang-53', 'Lang-54', 'Lang-55', 'Lang-57', 'Lang-58', 'Lang-59', 'Lang-6', 'Lang-61', 'Lang-65', 'Lang-9',
        'Math-10', 'Math-101', 'Math-102', 'Math-103', 'Math-105', 'Math-106', 'Math-11', 'Math-13', 'Math-17', 'Math-19', 'Math-2', 'Math-20', 'Math-21', 'Math-23', 'Math-24', 'Math-25', 'Math-26', 'Math-27', 'Math-28', 'Math-3', 'Math-30', 'Math-31', 'Math-32', 'Math-33', 'Math-34', 'Math-38', 'Math-39', 'Math-40', 'Math-41', 'Math-42', 'Math-43', 'Math-44', 'Math-45', 'Math-48', 'Math-5', 'Math-50', 'Math-51', 'Math-52', 'Math-53', 'Math-55', 'Math-56', 'Math-57', 'Math-58', 'Math-59', 'Math-60', 'Math-63', 'Math-64', 'Math-69', 'Math-7', 'Math-70', 'Math-72', 'Math-73', 'Math-74', 'Math-75', 'Math-78', 'Math-79', 'Math-8', 'Math-80', 'Math-82', 'Math-84', 'Math-85', 'Math-86', 'Math-87', 'Math-88', 'Math-89', 'Math-9', 'Math-90', 'Math-91', 'Math-94', 'Math-95', 'Math-96', 'Math-97',
        'Mockito-1', 'Mockito-12', 'Mockito-13', 'Mockito-18', 'Mockito-20', 'Mockito-22', 'Mockito-24', 'Mockito-27', 'Mockito-28', 'Mockito-29', 'Mockito-33', 'Mockito-34', 'Mockito-38', 'Mockito-5', 'Mockito-7', 'Mockito-8',
        'Time-14', 'Time-15', 'Time-16', 'Time-17', 'Time-18', 'Time-19', 'Time-20', 'Time-22', 'Time-23', 'Time-24', 'Time-25', 'Time-27', 'Time-4', 'Time-5', 'Time-7', 'Time-8'
        ]
v2_file_list = ['Cli-11', 'Cli-12', 'Cli-14', 'Cli-15', 'Cli-17', 'Cli-19', 'Cli-20', 'Cli-23', 'Cli-24', 'Cli-25', 'Cli-26', 'Cli-27', 'Cli-28', 'Cli-29', 'Cli-32', 'Cli-35', 'Cli-37', 'Cli-38', 'Cli-4', 'Cli-40', 'Cli-5', 'Cli-8', 'Cli-9',
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

def split_camel_case(name):
    words = []
    start = 0
    for i in range(1, len(name)):
        if name[i].isupper() and (name[i - 1].islower() or (i < len(name) - 1 and name[i + 1].islower())):
            words.append(name[start:i])
            start = i
    words.append(name[start:])
    return words

if __name__ == '__main__':
    datasets = parse_d4j(folder="./repair_data/")
    res_dict = {}
    norelated_method_list = []
    norelated_method_dict = {}
    model = ChatOpenAI(model="gpt-3.5-turbo", temperature=0, openai_api_key=openai_api_key)
    set_verbose(True)
    for idx, (data_name, dataset) in enumerate(datasets.items()):
        len_norelated_method = 0
        method_list = []
        class_list = []
        constructor_list = []
        node_list = []
        member_list = []
        inner_members = []
        outer_members = []

        print("============================= new =================================")
        print("idx: ", idx)
        print("data_name: ", data_name)
        buggy = dataset['buggy']
        print('buggy: ', buggy)

        with open("./repair_data/Defects4j" + "/single_function_repair.json", "r") as f:
            bug_dict = json.load(f)

        bug_id = data_name.split('.')[0]
        project = bug_id.split("-")[0]
        bug = bug_id.split("-")[1]
        tmp_bug_id = "test_" + bug_id
        start = bug_dict[bug_id]['start']
        end = bug_dict[bug_id]['end']

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

        subprocess.run('rm -rf ' + '/tmp/' + tmp_bug_id, shell=True)
        source_code = "".join(source)
        print("source_code: \n", source_code)


        print("-----------------------  Get identifier  -----------------------")
        # identify and filter related methods based on identifiers extracted from Java code.
        tokens = list(javalang.tokenizer.tokenize(buggy))
        identifiers = extract_identifiers(tokens)

        structural_path = "path to StructureInfo"
        with open(structural_path + bug_id + ".json", "r") as f:
            loaded_dist = json.load(f)

        loaded_method = loaded_dist["Method"]
        related_methods = []
        related_methods = [
            method for method in loaded_method
            if any(
                len(split_camel_case(method.lower().replace(identifier.lower(), '', 1))) == 1
                for identifier in identifiers if identifier.lower() in method.lower()
            ) or len(split_camel_case(method)) == 1
        ]

        if len(related_methods) == 0:
            len_norelated_method += 1
            norelated_method_list.append(data_name)
        related_path = "path to RelatedMethods"
        with open(related_path + bug_id + ".txt", "w") as f:
            f.write(str(related_methods))


        # print("-----------------------  Method Supplement  -----------------------")
        # chosen_path = "path to CandidatesMethods"
        # related_path = "path to RelatedMethods"
        # with open(related_path + bug_id + ".txt", "r") as f:
        #     text = f.read()
        #     # print("text: ", text)
        #     related_methods = ast.literal_eval(text)
        # print("related_methods: ", type(related_methods), related_methods)
        # chosen_methods = {}
        # if related_methods == []:
        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_methods, f)
        # else:
        #     for method_key in related_methods:
        #         print(f"Key: {method_key}")
        #         asts = extract_ast(source_code)
        #         find_node_in_ast(asts, method_key)
        #         for node in node_list:
        #             method_code1 = extract_method_code(source_code, node)
        #             if method_code1:
        #                 print("method_code1: \n", method_code1)
        #                 method_code = method_code1
        #         for member in member_list:
        #             method_code2 = extract_method_code(source_code, member)
        #             if method_code2:
        #                 print("method_code2: \n", method_code2)
        #                 method_code = method_code2
        #         chosen_methods[method_key] = method_code
        #     print("chosen_methods: \n", chosen_methods)

        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_methods, f)

        # print("-----------------------  Class Supplement  -----------------------")
        # def find_matching_brace(code, start):
        #     """
        #     Finds the position of the matching closing brace for the opening brace at code[start].
        #     """
        #     brace_count = 0
        #     for i in range(start, len(code)):
        #         if code[i] == '{':
        #             brace_count += 1
        #         elif code[i] == '}':
        #             brace_count -= 1
        #             if brace_count == 0:
        #                 return i
        #     return -1  # no matching brace found

        # def omit_code_bodies(code: str) -> str:
        #     result = []
        #     i = 0
        #     while i < len(code):
        #         if code[i:i+6] == 'class ' or code[i:i+7] == 'static ':
        #             match = re.match(r'(class|static\s+class)\s+\w+\s*(\([^)]*\))?\s*\{', code[i:])
        #             if match:
        #                 # Append the matched class/method declaration
        #                 result.append(match.group(0))
        #                 # Find the matching closing brace for this block
        #                 start = i + match.end(0) - 1
        #                 end = find_matching_brace(code, start)
        #                 if end != -1:
        #                     # Add the replacement text and the closing brace
        #                     result.append("\n         //The specific code has been omitted, and no errors are present.\n        }")
        #                     i = end + 1
        #                     continue
        #         elif re.match(r'\w+\s*\([^)]*\)\s*{', code[i:]):
        #             # Matches method declarations
        #             match = re.match(r'\w+\s*\([^)]*\)\s*{', code[i:])
        #             if match:
        #                 # Append the matched method declaration
        #                 result.append(match.group(0))
        #                 # Find the matching closing brace for this method
        #                 start = i + match.end(0) - 1
        #                 end = find_matching_brace(code, start)
        #                 if end != -1:
        #                     # Add the replacement text and the closing brace
        #                     result.append("\n         //The specific code has been omitted, but there is no error\n        }")
        #                     i = end + 1
        #                     continue
        #         # If no match, just add the current character to the result
        #         result.append(code[i])
        #         i += 1
        #     return ''.join(result)

        # chosen_path = "path to CandidatesClasses"
        # related_path = "path to StructureInfo"
        # with open(related_path + bug_id + ".json", "r") as f:
        #     related_classes = json.load(f)["Class"]
        # print("related_classes: ", type(related_classes), related_classes)
        # chosen_classes = {}
        # if related_classes == []:
        #     print("related_classes is null")
        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_classes, f)
        # else:
        #     for class_key in related_classes:
        #         print(f"Key: {class_key}")
        #         asts = extract_ast(source_code)
        #         find_node_in_ast(asts, class_key)
        #         for node in node_list:
        #             class_code1 = extract_method_code(source_code, node)
        #             if class_code1:
        #                 print("class_code1: \n", class_code1)
        #                 class_code = class_code1
        #         for inner in inner_members:
        #             class_code2 = extract_method_code(source_code, inner)
        #             if class_code2:
        #                 print("class_code2: \n", class_code2)
        #                 class_code = class_code2
        #         for outer in outer_members:
        #             class_code3 = extract_method_code(source_code, outer)
        #             if class_code3:
        #                 class_code = omit_code_bodies(class_code3)
        #                 print("class_code3: \n", class_code)
        #         chosen_classes[class_key] = class_code
        #     print("chosen_classes: \n", chosen_classes)

        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_classes, f)

        # print("-----------------------  Constructor Supplement  -----------------------")
        # chosen_path = "path to CandidatesConstructors"
        # related_path = "path to StructureInfo"
        # with open(related_path + bug_id + ".json", "r") as f:
        #     related_constructors = json.load(f)["Constructor"]
        # print("related_constructors: ", type(related_constructors), related_constructors)
        # chosen_constructors = {}
        # if related_constructors == []:
        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_constructors, f)
        # else:
        #     for constructor_key in related_constructors:
        #         print(f"Key: {constructor_key}")
        #         asts = extract_ast(source_code)
        #         find_node_in_ast(asts, constructor_key)
        #         for node in node_list:
        #             constructor_code1 = extract_method_code(source_code, node)
        #             if constructor_code1:
        #                 print("constructor_code1: \n", constructor_code1)
        #                 constructor_code = constructor_code1
        #         for member in inner_members:
        #             constructor_code2 = extract_method_code(source_code, member)
        #             if constructor_code2:
        #                 print("constructor_code2: \n", constructor_code2)
        #                 constructor_code = constructor_code2
        #         if constructor_code:
        #             chosen_constructors[constructor_key] = constructor_code
        #         else:
        #             chosen_constructors = {}
        #     print("chosen_constructors: \n", chosen_constructors)

        #     with open(chosen_path + bug_id + ".json", "w") as f:
        #         json.dump(chosen_constructors, f)
