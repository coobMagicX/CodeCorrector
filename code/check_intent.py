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
from repair_utils import parse_d4j, load_data, save_to_json, get_unified_diff, save_results, get_method, validate_patche, _run_validation, run_d4j_test

file_list = ['Chart-1', 'Chart-10', 'Chart-11', 'Chart-12', 'Chart-13', 'Chart-17', 'Chart-20', 'Chart-24', 'Chart-26', 'Chart-3', 'Chart-4', 'Chart-5', 'Chart-6', 'Chart-7', 'Chart-8', 'Chart-9',
        'Closure-1', 'Closure-10', 'Closure-101', 'Closure-102', 'Closure-104', 'Closure-105', 'Closure-107', 'Closure-109', 'Closure-11', 'Closure-111', 'Closure-112', 'Closure-113', 'Closure-114', 'Closure-115', 'Closure-116', 'Closure-117', 'Closure-118', 'Closure-119', 'Closure-12', 'Closure-120', 'Closure-121', 'Closure-122', 'Closure-123', 'Closure-124', 'Closure-125', 'Closure-126', 'Closure-128', 'Closure-129', 'Closure-13', 'Closure-130', 'Closure-131', 'Closure-132', 'Closure-133', 'Closure-14', 'Closure-15', 'Closure-17', 'Closure-18', 'Closure-19', 'Closure-2', 'Closure-20', 'Closure-21', 'Closure-22', 'Closure-23', 'Closure-24', 'Closure-25', 'Closure-29', 'Closure-31', 'Closure-32', 'Closure-33', 'Closure-35', 'Closure-36', 'Closure-38', 'Closure-39', 'Closure-4', 'Closure-40', 'Closure-42', 'Closure-44', 'Closure-48', 'Closure-5', 'Closure-50', 'Closure-51', 'Closure-52', 'Closure-53', 'Closure-55', 'Closure-56', 'Closure-57', 'Closure-58', 'Closure-59', 'Closure-61', 'Closure-62', 'Closure-65', 'Closure-66', 'Closure-67', 'Closure-69', 'Closure-7', 'Closure-70', 'Closure-71', 'Closure-73', 'Closure-77', 'Closure-78', 'Closure-81', 'Closure-82', 'Closure-83', 'Closure-86', 'Closure-87', 'Closure-88', 'Closure-91', 'Closure-92', 'Closure-94', 'Closure-95', 'Closure-96', 'Closure-97', 'Closure-99',
        'Lang-1', 'Lang-10', 'Lang-11', 'Lang-12', 'Lang-14', 'Lang-16', 'Lang-17', 'Lang-18', 'Lang-19', 'Lang-21', 'Lang-22', 'Lang-24', 'Lang-26', 'Lang-27', 'Lang-28', 'Lang-29', 'Lang-3', 'Lang-31', 'Lang-33', 'Lang-37', 'Lang-38', 'Lang-39', 'Lang-40', 'Lang-42', 'Lang-43', 'Lang-44', 'Lang-45', 'Lang-48', 'Lang-49', 'Lang-5', 'Lang-51', 'Lang-52', 'Lang-53', 'Lang-54', 'Lang-55', 'Lang-57', 'Lang-58', 'Lang-59', 'Lang-6', 'Lang-61', 'Lang-65', 'Lang-9',
        'Math-10', 'Math-101', 'Math-102', 'Math-103', 'Math-105', 'Math-106', 'Math-11', 'Math-13', 'Math-17', 'Math-19', 'Math-2', 'Math-20', 'Math-21', 'Math-23', 'Math-24', 'Math-25', 'Math-26', 'Math-27', 'Math-28', 'Math-3', 'Math-30', 'Math-31', 'Math-32', 'Math-33', 'Math-34', 'Math-38', 'Math-39', 'Math-40', 'Math-41', 'Math-42', 'Math-43', 'Math-44', 'Math-45', 'Math-48', 'Math-5', 'Math-50', 'Math-51', 'Math-52', 'Math-53', 'Math-55', 'Math-56', 'Math-57', 'Math-58', 'Math-59', 'Math-60', 'Math-63', 'Math-64', 'Math-69', 'Math-7', 'Math-70', 'Math-72', 'Math-73', 'Math-74', 'Math-75', 'Math-78', 'Math-79', 'Math-8', 'Math-80', 'Math-82', 'Math-84', 'Math-85', 'Math-86', 'Math-87', 'Math-88', 'Math-89', 'Math-9', 'Math-90', 'Math-91', 'Math-94', 'Math-95', 'Math-96', 'Math-97',
        'Mockito-1', 'Mockito-12', 'Mockito-13', 'Mockito-18', 'Mockito-20', 'Mockito-22', 'Mockito-24', 'Mockito-27', 'Mockito-28', 'Mockito-29', 'Mockito-33', 'Mockito-34', 'Mockito-38', 'Mockito-5', 'Mockito-7', 'Mockito-8',
        'Time-14', 'Time-15', 'Time-16', 'Time-17', 'Time-18', 'Time-19', 'Time-20', 'Time-22', 'Time-23', 'Time-24', 'Time-25', 'Time-27', 'Time-4', 'Time-5', 'Time-7', 'Time-8'
        ]

exclude_list = ["Chart-10", "Chart-11", "Closure-111", "Closure-82", "Lang-14", "Math-105", "Math-2", "Time-15",
    "Lang-57","Math-101","Math-25","Math-34","Math-41","Math-45","Math-50","Math-86","Mockito-8"]


openai_api_key = 'your key'


datasets = parse_d4j(folder="./repair_data/")
res_dict = {}
model = ChatOpenAI(model="gpt-3.5-turbo-16k", openai_api_key=openai_api_key)
set_verbose(True)
for idx, (data_name, dataset) in enumerate(datasets.items()):
    print("idx: ", idx)
    print("data_name: ", data_name)
    if data_name.split('.')[0] not in file_list or data_name.split('.')[0] in exclude_list:
        continue
    buggy = dataset['buggy']
    print('buggy: ', buggy)
    method_name = get_method(buggy)
    print('method_name: ', method_name)

    bug_id = data_name.split('.')[0]

    testcase_path = "./repair_data/repair_test_json/FailTestCase/Code/2.0/"
    with open(testcase_path + "fail_test_case.json", "r") as f:
        testcase_dict = json.load(f)
    tracecode_path = "./repair_data/repair_test_json/FailTestCase/Line/2.0/"
    with open(tracecode_path + bug_id + ".json", "r") as f:
        matching_codes = json.load(f)

    for t in testcase_dict[bug_id]:
        testcase_split = t.split("::")
        print("testcase_split: ", testcase_split)
        testcase_name = testcase_split[-1]
        # 检查testcase_name是否以'\n'结尾并处理
        if testcase_name.endswith('\n'):
            testcase_name = testcase_name[:-1]
        print("testcase_name: ", testcase_name)

        with open(testcase_path + bug_id + "_" + testcase_name + ".java", "r") as f:
            testcase_code = f.read()
        print("testcase_code: \n", testcase_code)

        try:
            trace_line = matching_codes[testcase_name]
        except:
            continue

        if trace_line:
            print("trace_line: ", trace_line)

            intent_prompt = ChatPromptTemplate.from_messages([
            ("system", "You are a code testing and analysis assistant."),
            ("user","""
            Infer the purpose and intent of the failing test cases.
            Analyze failed test cases and infer potential errors in the source code.
            Based on this analysis, provide a brief outline of key repair direction that can help the source code pass the test case.
            (without details of the repair, just clearly state the direction for the repair)

            Desired format:
            Intent:<put the intent and purpose here>.
            Repair strategy:<put the repair strategy here>.

            Source code: 
            {code}

            Failed test cases: 
            {test_case}

            Fault-revealing lines of failed test cases:
            {trace_line}
            """)])
            intent_chain = LLMChain(prompt=intent_prompt, llm=model, output_key="intent", verbose=True)
            res = intent_chain.invoke({"code": buggy, "test_case": testcase_code, "trace_line": trace_line})
            intent_res = res["intent"]
            print("--intent_res:\n", intent_res)

            intent_path = "./repair_data/repair_test_json/TestCaseIntent/res/"
            with open(intent_path + bug_id + "_" + testcase_name + ".txt", "w") as f:
                f.write(intent_res)
