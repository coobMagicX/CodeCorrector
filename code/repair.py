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
import argparse
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
from repair_utils import parse_d4j, load_data, save_to_json, get_unified_diff, save_results, get_method, validate_patche, _run_validation, run_d4j_test, may_pass


file_list = ['Chart-1', 'Chart-10', 'Chart-11', 'Chart-12', 'Chart-13', 'Chart-17', 'Chart-20', 'Chart-24', 'Chart-26', 'Chart-3', 'Chart-4', 'Chart-5', 'Chart-6', 'Chart-7', 'Chart-8', 'Chart-9',
        'Closure-1', 'Closure-10', 'Closure-101', 'Closure-102', 'Closure-104', 'Closure-105', 'Closure-107', 'Closure-109', 'Closure-11', 'Closure-111', 'Closure-112', 'Closure-113', 'Closure-114', 'Closure-115', 'Closure-116', 'Closure-117', 'Closure-118', 'Closure-119', 'Closure-12', 'Closure-120', 'Closure-121', 'Closure-122', 'Closure-123', 'Closure-124', 'Closure-125', 'Closure-126', 'Closure-128', 'Closure-129', 'Closure-13', 'Closure-130', 'Closure-131', 'Closure-132', 'Closure-133', 'Closure-14', 'Closure-15', 'Closure-17', 'Closure-18', 'Closure-19', 'Closure-2', 'Closure-20', 'Closure-21', 'Closure-22', 'Closure-23', 'Closure-24', 'Closure-25', 'Closure-29', 'Closure-31', 'Closure-32', 'Closure-33', 'Closure-35', 'Closure-36', 'Closure-38', 'Closure-39', 'Closure-4', 'Closure-40', 'Closure-42', 'Closure-44', 'Closure-48', 'Closure-5', 'Closure-50', 'Closure-51', 'Closure-52', 'Closure-53', 'Closure-55', 'Closure-56', 'Closure-57', 'Closure-58', 'Closure-59', 'Closure-61', 'Closure-62', 'Closure-65', 'Closure-66', 'Closure-67', 'Closure-69', 'Closure-7', 'Closure-70', 'Closure-71', 'Closure-73', 'Closure-77', 'Closure-78', 'Closure-81', 'Closure-82', 'Closure-83', 'Closure-86', 'Closure-87', 'Closure-88', 'Closure-91', 'Closure-92', 'Closure-94', 'Closure-95', 'Closure-96', 'Closure-97', 'Closure-99',
        'Lang-1', 'Lang-10', 'Lang-11', 'Lang-12', 'Lang-14', 'Lang-16', 'Lang-17', 'Lang-18', 'Lang-19', 'Lang-21', 'Lang-22', 'Lang-24', 'Lang-26', 'Lang-27', 'Lang-28', 'Lang-29', 'Lang-3', 'Lang-31', 'Lang-33', 'Lang-37', 'Lang-38', 'Lang-39', 'Lang-40', 'Lang-42', 'Lang-43', 'Lang-44', 'Lang-45', 'Lang-48', 'Lang-49', 'Lang-5', 'Lang-51', 'Lang-52', 'Lang-53', 'Lang-54', 'Lang-55', 'Lang-57', 'Lang-58', 'Lang-59', 'Lang-6', 'Lang-61', 'Lang-65', 'Lang-9',
        'Math-10', 'Math-101', 'Math-102', 'Math-103', 'Math-105', 'Math-106', 'Math-11', 'Math-13', 'Math-17', 'Math-19', 'Math-2', 'Math-20', 'Math-21', 'Math-23', 'Math-24', 'Math-25', 'Math-26', 'Math-27', 'Math-28', 'Math-3', 'Math-30', 'Math-31', 'Math-32', 'Math-33', 'Math-34', 'Math-38', 'Math-39', 'Math-40', 'Math-41', 'Math-42', 'Math-43', 'Math-44', 'Math-45', 'Math-48', 'Math-5', 'Math-50', 'Math-51', 'Math-52', 'Math-53', 'Math-55', 'Math-56', 'Math-57', 'Math-58', 'Math-59', 'Math-60', 'Math-63', 'Math-64', 'Math-69', 'Math-7', 'Math-70', 'Math-72', 'Math-73', 'Math-74', 'Math-75', 'Math-78', 'Math-79', 'Math-8', 'Math-80', 'Math-82', 'Math-84', 'Math-85', 'Math-86', 'Math-87', 'Math-88', 'Math-89', 'Math-9', 'Math-90', 'Math-91', 'Math-94', 'Math-95', 'Math-96', 'Math-97',
        'Mockito-1', 'Mockito-12', 'Mockito-13', 'Mockito-18', 'Mockito-20', 'Mockito-22', 'Mockito-24', 'Mockito-27', 'Mockito-28', 'Mockito-29', 'Mockito-33', 'Mockito-34', 'Mockito-38', 'Mockito-5', 'Mockito-7', 'Mockito-8',
        'Time-14', 'Time-15', 'Time-16', 'Time-17', 'Time-18', 'Time-19', 'Time-20', 'Time-22', 'Time-23', 'Time-24', 'Time-25', 'Time-27', 'Time-4', 'Time-5', 'Time-7', 'Time-8'
        ]
exclude_list = ["Chart-10", "Chart-11", "Closure-111", "Closure-82", "Lang-14", "Math-105", "Math-2", "Time-15",
    "Lang-57","Math-101","Math-25","Math-34","Math-41","Math-45","Math-50","Math-86","Mockito-8",
    "Lang-51"]

openai_api_key = 'your key'


def load_json(file_path):
    """Load JSON data from file."""
    with open(file_path, "r") as f:
        return json.load(f)


def load_text(file_path):
    """Load text data from file."""
    with open(file_path, "r") as f:
        return f.read()


def repair_code(agent_executor, memory, buggy, intents, additional_methods):
    """Repair buggy code using agent executor."""
    repair_input = f"""
    Repair the buggy source code based on the provided Methods in the context and the intent of failed testcase.
    Only use the existing methods and data for the repair.
    Limit your modifications to the problematic source code section.

    Desired format:
    ```java
    <put the full fixed code here>
    ```

    Intent of failed testcase:{intents}

    The buggy source code: 
    {buggy}

    Methods that you can utilize in the context:
    {additional_methods}
    """
    config = {"configurable": {"session_id": "test-session"}}
    chain_with_message_history = RunnableWithMessageHistory(
        agent_executor,
        lambda session_id: memory,
        input_messages_key="input",
        history_messages_key="chat_history",
        verbose=True
    )
    response = chain_with_message_history.invoke({"input": repair_input}, config)
    return response


def process_dataset(data_name, dataset, bug_dict, testcase_dict, model, tools):
    """Process a single dataset and attempt code repair."""
    buggy = dataset['buggy']
    bug_id = data_name.split('.')[0]
    intents, additional_methods = "", ""

    for t in testcase_dict.get(bug_id, []):
        testcase_name = t.split("::")[-1].strip()
        intent_path = f"path to Intent/{bug_id}_{testcase_name}.txt"
        select_path = f"path to ChosenMethods/{bug_id}_{testcase_name}.txt"

        try:
            intents = load_text(intent_path)
            select_res = load_text(select_path)
        except FileNotFoundError:
            continue

        # Parse selected methods
        select_pattern = re.compile(r"(\d+)-(.*?)(?=\n|\d+-|$)", re.DOTALL)
        select_matches = select_pattern.findall(select_res)

        # Load additional methods
        chosen_methods = load_json(f"path to CandidatesMethods/{data_name.split('.')[0]}.json")
        for sl in select_matches:
            key = sl[1]
            if key in chosen_methods:
                additional_methods += chosen_methods[key] + "\n"

        # Initialize repair process
        memory = ChatMessageHistory(session_id="test-session")
        repair_prompt = ChatPromptTemplate.from_messages([
            ("system", "You are an APR tool."),
            ("placeholder", "{chat_history}"),
            ("user", "{input}"),
            ("placeholder", "{agent_scratchpad}")
        ])
        agent = create_tool_calling_agent(model, tools, repair_prompt)
        agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True)

        # Repair code
        response = repair_code(agent_executor, memory, buggy, intents, additional_methods)
        repair_res = response["output"]

        # Extract repair result
        pattern = r"```java\n(.*?)\n```"
        try:
            repair_match = re.findall(pattern, repair_res, re.DOTALL)[0]
            well = 1
        except IndexError:
            repair_match = ""
            well = 0

        # Save results
        save_results(data_name, buggy, repair_match, well)
        test_result, javac_error = validate_patche(data_name)

        if test_result == "success":
            return True
    return False


def main(args):
    datasets = parse_d4j(folder=args.folder)
    bug_dict = load_json(args.bug_dict)
    testcase_dict = load_json(args.testcase_dict)

    model = ChatOpenAI(model="gpt-3.5-turbo-16k", openai_api_key=args.api_key)
    tools = []
    success_files = []

    for idx, (data_name, dataset) in enumerate(datasets.items()):
        print(f"Processing dataset {idx}: {data_name}")
        if data_name.split('.')[0] in args.exclude_list:
            continue

        if process_dataset(data_name, dataset, bug_dict, testcase_dict, model, tools):
            success_files.append(data_name)

    print("Successfully repaired files:", success_files)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Automated Program Repair Tool")
    parser.add_argument("--folder", type=str, required=True, help="Path to dataset folder")
    parser.add_argument("--bug-dict", type=str, required=True, help="Path to bug dictionary JSON file")
    parser.add_argument("--testcase-dict", type=str, required=True, help="Path to test case dictionary JSON file")
    parser.add_argument("--api-key", type=str, required=True, help="OpenAI API key")
    parser.add_argument("--exclude-list", type=str, nargs="*", default=[], help="List of files to exclude")

    args = parser.parse_args()
    main(args)