# -*- coding: utf-8 -*-
"""
Created on 22/6/2023 下午6:51

@author: Cai Muxin
@email: 1607494203@qq.com
"""
# 标准库导入
import json
import os
import re
import getpass
import signal
import subprocess
import argparse
import glob
from difflib import unified_diff
from pprint import pprint

# 第三方库导入
import torch
import tiktoken
import javalang

# LangChain库导入
from langchain_community.chat_message_histories import ChatMessageHistory
from langchain_core.runnables.history import RunnableWithMessageHistory
from langchain_huggingface import HuggingFacePipeline
from langchain_core.tools import tool
from langchain.agents import AgentExecutor, create_tool_calling_agent
from langchain_core.prompts import ChatPromptTemplate
from langchain.chains.llm import LLMChain
from langchain.chains.sequential import SimpleSequentialChain, SequentialChain
from langchain_openai import ChatOpenAI
from langchain.globals import set_verbose
from langchain_core.output_parsers import StrOutputParser
from langchain_ollama import ChatOllama

# Hugging Face库导入，用于本地模型的调用
from transformers import AutoModelForCausalLM, AutoTokenizer, pipeline, BitsAndBytesConfig

# 本地工具导入
from repair_utils import clean_parse_d4j, load_data, save_to_json, get_unified_diff, save_results, get_method, validate_patche, _run_validation, run_d4j_test, may_pass

file_list = ['Chart-1', 'Chart-10', 'Chart-11', 'Chart-12', 'Chart-13', 'Chart-17', 'Chart-20', 'Chart-24', 'Chart-26', 'Chart-3', 'Chart-4', 'Chart-5', 'Chart-6', 'Chart-7', 'Chart-8', 'Chart-9',
        'Closure-1', 'Closure-10', 'Closure-101', 'Closure-102', 'Closure-104', 'Closure-105', 'Closure-107', 'Closure-109', 'Closure-11', 'Closure-111', 'Closure-112', 'Closure-113', 'Closure-114', 'Closure-115', 'Closure-116', 'Closure-117', 'Closure-118', 'Closure-119', 'Closure-12', 'Closure-120', 'Closure-121', 'Closure-122', 'Closure-123', 'Closure-124', 'Closure-125', 'Closure-126', 'Closure-128', 'Closure-129', 'Closure-13', 'Closure-130', 'Closure-131', 'Closure-132', 'Closure-133', 'Closure-14', 'Closure-15', 'Closure-17', 'Closure-18', 'Closure-19', 'Closure-2', 'Closure-20', 'Closure-21', 'Closure-22', 'Closure-23', 'Closure-24', 'Closure-25', 'Closure-29', 'Closure-31', 'Closure-32', 'Closure-33', 'Closure-35', 'Closure-36', 'Closure-38', 'Closure-39', 'Closure-4', 'Closure-40', 'Closure-42', 'Closure-44', 'Closure-48', 'Closure-5', 'Closure-50', 'Closure-51', 'Closure-52', 'Closure-53', 'Closure-55', 'Closure-56', 'Closure-57', 'Closure-58', 'Closure-59', 'Closure-61', 'Closure-62', 'Closure-65', 'Closure-66', 'Closure-67', 'Closure-69', 'Closure-7', 'Closure-70', 'Closure-71', 'Closure-73', 'Closure-77', 'Closure-78', 'Closure-81', 'Closure-82', 'Closure-83', 'Closure-86', 'Closure-87', 'Closure-88', 'Closure-91', 'Closure-92', 'Closure-94', 'Closure-95', 'Closure-96', 'Closure-97', 'Closure-99',
        'Lang-1', 'Lang-10', 'Lang-11', 'Lang-12', 'Lang-14', 'Lang-16', 'Lang-17', 'Lang-18', 'Lang-19', 'Lang-21', 'Lang-22', 'Lang-24', 'Lang-26', 'Lang-27', 'Lang-28', 'Lang-29', 'Lang-3', 'Lang-31', 'Lang-33', 'Lang-37', 'Lang-38', 'Lang-39', 'Lang-40', 'Lang-42', 'Lang-43', 'Lang-44', 'Lang-45', 'Lang-48', 'Lang-49', 'Lang-5', 'Lang-51', 'Lang-52', 'Lang-53', 'Lang-54', 'Lang-55', 'Lang-57', 'Lang-58', 'Lang-59', 'Lang-6', 'Lang-61', 'Lang-65', 'Lang-9',
        'Math-10', 'Math-101', 'Math-102', 'Math-103', 'Math-105', 'Math-106', 'Math-11', 'Math-13', 'Math-17', 'Math-19', 'Math-2', 'Math-20', 'Math-21', 'Math-23', 'Math-24', 'Math-25', 'Math-26', 'Math-27', 'Math-28', 'Math-3', 'Math-30', 'Math-31', 'Math-32', 'Math-33', 'Math-34', 'Math-38', 'Math-39', 'Math-40', 'Math-41', 'Math-42', 'Math-43', 'Math-44', 'Math-45', 'Math-48', 'Math-5', 'Math-50', 'Math-51', 'Math-52', 'Math-53', 'Math-55', 'Math-56', 'Math-57', 'Math-58', 'Math-59', 'Math-60', 'Math-63', 'Math-64', 'Math-69', 'Math-7', 'Math-70', 'Math-72', 'Math-73', 'Math-74', 'Math-75', 'Math-78', 'Math-79', 'Math-8', 'Math-80', 'Math-82', 'Math-84', 'Math-85', 'Math-86', 'Math-87', 'Math-88', 'Math-89', 'Math-9', 'Math-90', 'Math-91', 'Math-94', 'Math-95', 'Math-96', 'Math-97',
        'Mockito-1', 'Mockito-12', 'Mockito-13', 'Mockito-18', 'Mockito-20', 'Mockito-22', 'Mockito-24', 'Mockito-27', 'Mockito-28', 'Mockito-29', 'Mockito-33', 'Mockito-34', 'Mockito-38', 'Mockito-5', 'Mockito-7', 'Mockito-8',
        'Time-14', 'Time-15', 'Time-16', 'Time-17', 'Time-18', 'Time-19', 'Time-20', 'Time-22', 'Time-23', 'Time-24', 'Time-25', 'Time-27', 'Time-4', 'Time-5', 'Time-7', 'Time-8'
        ]
# file_list = ["Lang-53"]
exclude_list = ["Chart-10", "Chart-11", "Closure-111", "Closure-82", "Lang-14", "Math-105", "Math-2", "Time-15",
    "Lang-57","Math-101","Math-25","Math-34","Math-41","Math-45","Math-50","Math-86","Mockito-8",
    "Lang-51"] #上下文短/testcase未找到/上下文无补充


# ---------------------- Ollama ---------------------------
llm = ChatOllama(
    base_url='http://127.0.0.1:11434',
    model = "llama3.1",
    temperature=0.7,
    # num_gpu=2
    )

def remove_comments(string):
    pattern = r"(\".*?\"|\'.*?\')|(/\*.*?\*/|//[^\r\n]*$)"
    def _replacer(match):
        if match.group(0).startswith(("//", "/*")):
            return "" # so we will return empty to remove the comment
        else: # otherwise, we will return the 1st group
            return match.group(0) # captured quoted-string
    value = re.compile(pattern, re.MULTILINE|re.DOTALL)
    return value.sub(_replacer, string)

def get_addition(select_res, base_path, data_name):
    print("--select_res: \n", select_res)
    if ": " in select_res:
        print(":")
        select_pattern = re.compile(r": (.*?)(?=\n|\d+-|$)",re.DOTALL)
    elif ":**" in select_res:
        print(":\*\*")
        select_pattern = re.compile(r":\*\* (.*?)(?=\n|\d+-|$)",re.DOTALL)
    else:
        print("-")
        select_pattern = re.compile(r"-(.*?)(?=\n|\d+-|$)",re.DOTALL)
    select_matches = select_pattern.findall(select_res)
    print("--select_matches: \n", select_matches)

    class_path = base_path + "/repair_data/repair_test_json/ChosenMethods" + "/CandidatesClasses/1.2/"
    with open(class_path + data_name.split('.')[0] + ".json", "r") as f:
        chosen_classes = json.load(f)
    cons_path = base_path + "/repair_data/repair_test_json/ChosenMethods" + "/CandidatesConstructors/1.2/"
    with open(cons_path + data_name.split('.')[0] + ".json", "r") as f:
        chosen_constructors = json.load(f)
    method_path = base_path + "/repair_data/repair_test_json/ChosenMethods" + "/CandidatesMethods/1.2/"
    with open(method_path + data_name.split('.')[0] + ".json", "r") as f:
        chosen_methods = json.load(f)

    if len(select_matches) != 0:
        additional_methods = ""
        for sl in select_matches:
            try:
                key = sl
                print("key: ", key)
                #预处理key
                if re.search(r'`?([^`]+?)`?\.?\s*$', key):
                    key = re.search(r'`?([^`]+?)`?\.?\s*$', key).group(1)
                #匹配对应代码
                if key in chosen_classes:
                    value = remove_comments(chosen_classes[key])
                    print(value)
                    raise Exception("s")
                if key in chosen_constructors:
                    value = chosen_constructors[key]
                elif key in chosen_methods:
                    value = chosen_methods[key]
                else:
                    continue
            except:
                continue
            additional_methods = value + "\n" + additional_methods
    else:
        additional_methods = ""
        # raise Exception("stop")
    return additional_methods


def repair_execution_step(base_path, folder, file_list, exclude_list):
    token_files = {}
    datasets = clean_parse_d4j(folder=folder)
    for step in range(5, 11):
        once_success_file = []
        for idx, (data_name, dataset) in enumerate(datasets.items()):
            print("idx: ", idx)
            print("data_name: ", data_name)

            if data_name.split('.')[0] not in file_list or data_name.split('.')[0] in exclude_list:
                continue
            buggy = dataset['buggy']

            with open(base_path + "/repair_data/Defects4j/single_function_repair.json", "r") as f:
                bug_dict = json.load(f)

            bug_id = data_name.split('.')[0]
            project = bug_id.split("-")[0]
            bug = bug_id.split("-")[1]
            tmp_bug_id = "test_" + bug_id
            start = bug_dict[bug_id]['start']
            end = bug_dict[bug_id]['end']

            structural_path = base_path + "/repair_data/repair_test_json/StructureInfo/1.2/"
            with open(structural_path + bug_id + ".json", "r") as f:
                loaded_dist = json.load(f)
            print("loaded_method \n", loaded_dist["Method"])

            related_path = base_path + "/repair_data/repair_test_json/RelatedMethods/1.2/"
            with open(related_path + bug_id + ".txt", "r") as f:
                related_methods = f.read()

            if related_methods == "[]":
                related_methods = loaded_dist["Method"]
            related_classes = loaded_dist["Class"]
            related_constructors = loaded_dist["Constructor"]

            testcase_path = base_path + "/repair_data/repair_test_json/FailTestCase/Code/1.2/"
            with open(testcase_path + "fail_test_case.json", "r") as f:
                testcase_dict = json.load(f)

            for t in testcase_dict[bug_id]:
                testcase_split = t.split("::")
                testcase_name = testcase_split[-1]
                if testcase_name.endswith('\n'):
                    testcase_name = testcase_name[:-1]
                try:
                    intent_path = "./TestCaseIntent/res/llama3/1.2/{}/".format(step)
                    with open(intent_path + bug_id + "_" + testcase_name + ".txt", "r") as f:
                        intents = f.read()
                except FileNotFoundError:
                    continue

                select_path = "./Choose/res/llama3/1.2/{}/".format(step)
                with open(select_path + bug_id + "_" + testcase_name + ".txt", "r") as f:
                    select_res = f.read()

                additional_methods = get_addition(select_res, base_path)

                print("Additional_methods: \n", additional_methods)
                print(llm.num_gpu)

                # ---------------------- 用 LLM ---------------------------
                message = [
                    {"role": "system", "content": "You are an APR tool."},
                    {"role": "user", "content": """
                    Repair the buggy source code based on the provided Methods in the context and the intent of failed testcase.
                    Only use the existing methods and data for the repair.
                    Limit your modifications to the problematic source code section.

                    Desired format:
                    ```java
                    <put the full fixed code here>
                    ```

                    Intent of failed testcase:{intents}

                    The buggy source code: 
                    {code}

                    Methods that you can utilize in the context:
                    {additional_methods}
                    """.format(intents=intents, code=buggy, additional_methods=additional_methods)}
                ]
                response = llm.invoke(message).content
                print("--response:\n", response)
                result_path = "./results/llama3.1/1.2/{}/".format(step)

                # 捕获回复中的代码
                pattern = r"```java\n(.*?)\n```"
                try:
                    # 存在修复代码
                    repair_match = re.findall(pattern, response, re.DOTALL)[0]
                    well = 1
                except IndexError:
                    # 不存在修复代码
                    repair_match = ""
                    well = 0
                print("--repair_match:\n", repair_match)

                # ------------------处理结果----------------------
                save_results(data_name, buggy, repair_match, result_path, well)

                # ------------------执行修复----------------------
                test_result, javac_error = validate_patche(data_name, base_path, result_path)
                print("--test_result:\n", test_result)
                if test_result == "success":
                    print("---")
                    print("成功生成有效修复模板")
                    once_success_file.append(data_name)
                else:
                    print("---")
                    print("生成错误修复模板")
                break
        print("== once_success_file== : ", once_success_file)
        with open("./results/llama3.1/1.2/{}/record".format(step), "a") as f:
            f.write(str(once_success_file))

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Repair buggy code for multiple steps and validate patches.")
    parser.add_argument('--base_path', type=str, required=True, help="Base path for the project.")
    parser.add_argument('--folder', type=str, required=True, help="Folder path for the repair data.")
    parser.add_argument('--file_list', type=str, nargs='+', required=True, help="List of files to include.")
    parser.add_argument('--exclude_list', type=str, nargs='+', required=True, help="List of files to exclude.")

    args = parser.parse_args()

    repair_execution_step(
        base_path=args.base_path,
        folder=args.folder,
        file_list=args.file_list,
        exclude_list=args.exclude_list
    )
