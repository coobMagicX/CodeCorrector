# correct from plausible
import argparse
import json
import os
import re
import openai

from Dataset.dataset import parse_defects4j_12, parse_defects4j_2
from Dataset.dataset import parse_python, parse_java, get_unified_diff, remove_suffix, remove_prefix
from Generation.util.api_request import create_chatgpt_config, request_chatgpt_engine
from util.util import complex_chatgpt_parse, num_tokens_from_messages, simple_chatgpt_parse
from util.util import write_file, build_error_message_based_chatgpt_response_message
from prompt import INIT_CHATGPT_INFILL_PFC_PROMPT, PFC_SUFFIX_PROMPT, PFC_ADD_PROMPT
from prompt import INIT_CHATGPT_INFILL_LINE_PFC_PROMPT, PFC_SUFFIX_LINE_PROMPT
from prompt import INIT_CHATGPT_FUNCTION_PFC_PROMPT, PFC_SUFFIX_FUNCTION_PROMPT


def remove_comments(string):
    pattern = r"(\".*?\"|\'.*?\')|(/\*.*?\*/|//[^\r\n]*$)"
    # first group captures quoted strings (double or single)
    # second group captures comments (//single-line or /* multi-line */)
    regex = re.compile(pattern, re.MULTILINE|re.DOTALL)
    def _replacer(match):
        # if the 2nd group (capturing comments) is not None,
        # it means we have captured a non-quoted (real) comment string.
        if match.group(2) is not None:
            return "" # so we will return empty to remove the comment
        else: # otherwise, we will return the 1st group
            return match.group(1) # captured quoted-string
    return regex.sub(_replacer, string)


def plausible_to_correct(args, bugs):
    INFILL_TOKEN = ">>> [ INFILL ] <<<"
    if args.hunk:
        max_tokens = 200
    else:
        max_tokens = 100
    with open(args.target_folder + "/lm_repair.json", "r") as f:
        plausible_fixes = json.load(f)
        # underlying assumption is that there should be on plausible fix
        # and is computed using chatgpt

    results = {}
    # plausible_list = ["Chart-9.java", "Chart-10.java", "Closure-10.java", "Closure-12.java", "Closure-33.java",
    #                   "Closure-61.java", "Closure-70.java", "Closure-122.java", "Closure-124.java", "Closure-125.java",
    #                   "Closure-129.java", "Lang-22.java", "Lang-37.java", "Lang-51.java", "Lang-58.java", "Math-2.java",
    #                   "Math-3.java", "Math-19.java", "Math-30.java", "Math-32.java", "Math-39.java", "Math-48.java", "Math-63.java",
    #                   "Math-69.java", "Math-73.java", "Math-80.java", "Mockito-33.java", "Time-19.java"]
    # plausible_list = ["Chart-10.java", "Chart-13.java", "Closure-62.java", "Closure-73.java", "Closure-107.java",
    #                   "Closure-125.java", "Math-32.java", "Math-33.java", "Math-58.java", "Math-69.java",
    #                   "Math-75.java", "Time-19.java"]
    # plausible_list = ["Cli-11.java", "Cli-25.java", "Closure-168.java", "Codec-9.java", "Compress-19.java", "Compress-38.java",
    #                   "Csv-14.java", "JacksonCore-8.java", "JacksonCore-26.java", "JacksonDatabind-34.java", "JacksonDatabind-70.java",
    #                   "JacksonDatabind-71.java", "Jsoup-2.java", "Jsoup-24.java", "Jsoup-26.java", "Jsoup-34.java", "Jsoup-39.java",
    #                   "Jsoup-40.java", "Jsoup-41.java", "Jsoup-46.java", "Jsoup-51.java", "Jsoup-54.java"]
    # plausible_list = ["Chart-13.java", "Chart-10.java", "Closure-7.java", "Closure-10.java", "Closure-22.java", "Closure-38.java", "Closure-51.java",
    #                   "Closure-58.java", "Closure-77.java", "Closure-94.java", "Closure-104.java", "Closure-107.java",
    #                   "Closure-122.java", "Closure-124.java", "Closure-125.java", "Closure-129.java", "Closure-131.java",
    #                   "Lang-12.java", "Lang-14.java", "Lang-18.java", "Lang-28.java", "Lang-31.java", "Lang-37.java",
    #                   "Lang-39.java", "Lang-42.java", "Lang-43.java", "Lang-51.java", "Math-8.java", "Math-19.java",
    #                   "Math-20.java", "Math-23.java", "Math-25.java", "Math-26.java", "Math-28.java", "Math-32.java",
    #                   "Math-39.java", "Math-57.java", "Math-69.java", "Math-78.java", "Math-80.java", "Math-87.java",
    #                   "Math-88.java", "Math-94.java", "Math-97.java", "Math-101.java", "Time-18.java", "Time-20.java"]
    plausible_list = ['Chart-1.java', 'Chart-10.java', 'Chart-11.java', 'Chart-13.java', 'Chart-20.java', 'Chart-24'                                                                                         '.java',
                      'Chart-4.java', 'Chart-7.java', 'Chart-8.java', 'Chart-9.java', 'Closure-101.java',
                      'Closure-104.java', 'Closure-107.java', 'Closure-114.java', 'Closure-119.java',
                      'Closure-122.java', 'Closure-124.java', 'Closure-125.java', 'Closure-126.java',
                      'Closure-128.java', 'Closure-129.java', 'Closure-131.java', 'Closure-15.java',
                      'Closure-19.java', 'Closure-2.java', 'Closure-20.java', 'Closure-38.java', 'Closure-44.java',
                      'Closure-51.java', 'Closure-52.java', 'Closure-56.java', 'Closure-62.java', 'Closure-65.java',
                      'Closure-73.java', 'Closure-77.java', 'Closure-86.java', 'Closure-88.java', 'Closure-94.java',
                      'Closure-97.java', 'Lang-12.java', 'Lang-14.java', 'Lang-17.java', 'Lang-19.java',
                      'Lang-21.java', 'Lang-22.java', 'Lang-24.java', 'Lang-26.java', 'Lang-28.java', 'Lang-29.java',
                      'Lang-31.java', 'Lang-33.java', 'Lang-40.java', 'Lang-42.java', 'Lang-43.java', 'Lang-45.java',
                      'Lang-5.java', 'Lang-51.java', 'Lang-52.java', 'Lang-59.java', 'Lang-6.java', 'Lang-61.java',
                      'Math-101.java', 'Math-106.java', 'Math-11.java', 'Math-17.java', 'Math-2.java',
                      'Math-25.java', 'Math-26.java', 'Math-28.java', 'Math-3.java', 'Math-30.java', 'Math-32.java',
                      'Math-34.java', 'Math-41.java', 'Math-45.java', 'Math-5.java', 'Math-56.java', 'Math-57.java',
                      'Math-59.java', 'Math-60.java', 'Math-69.java', 'Math-72.java', 'Math-73.java', 'Math-8.java',
                      'Math-80.java', 'Math-82.java', 'Math-85.java', 'Math-86.java', 'Math-91.java', 'Math-94.java',
                      'Math-95.java', 'Math-97.java', 'Mockito-12.java', 'Mockito-13.java', 'Mockito-24.java',
                      'Mockito-29.java', 'Mockito-34.java', 'Mockito-38.java', 'Time-15.java', 'Time-16.java',
                      'Time-18.java', 'Time-20.java']

    print("checkpoint")
    for bug, v in plausible_fixes.items():
        if bug not in plausible_list:
            continue
        plausible_patch = ""
        for patch in v:
            if patch['valid']:
                plausible_patch = patch['patch']
                starting_try = patch['tries']
                break
        print("bug: ", bug)
        if plausible_patch != "":
            if args.hunk:
                prompt = INIT_CHATGPT_INFILL_PFC_PROMPT.format(
                    buggy_code=(bugs[bug]['prefix'] + "\n" + INFILL_TOKEN + "\n" + bugs[bug]['suffix']),
                    buggy_hunk=bugs[bug]['buggy_line'],
                    fix_hunk=plausible_patch.strip(),
                    failing_test=bugs[bug]['failing_tests'][0]['test_method_name'],
                    error_message=bugs[bug]['failing_tests'][0]['failure_message'].strip(),
                    failing_line=bugs[bug]['failing_tests'][0]['failing_line'].strip()
                ).strip()
            elif args.function:
                prompt = INIT_CHATGPT_FUNCTION_PFC_PROMPT.format(
                    buggy_code=bugs[bug]['buggy'],
                    patch_function=plausible_patch.strip(),
                    failing_test=bugs[bug]['failing_tests'][0]['test_method_name'],
                    error_message=bugs[bug]['failing_tests'][0]['failure_message'].strip(),
                    failing_line=bugs[bug]['failing_tests'][0]['failing_line'].strip()
                ).strip()
                # raise Exception("cfp")
            else:
                prompt = INIT_CHATGPT_INFILL_LINE_PFC_PROMPT.format(
                    buggy_code=(bugs[bug]['prefix'] + "\n" + INFILL_TOKEN + "\n" + bugs[bug]['suffix']),
                    buggy_hunk=bugs[bug]['buggy_line'],
                    fix_hunk=plausible_patch.strip(),
                    failing_test=bugs[bug]['failing_tests'][0]['test_method_name'],
                    error_message=bugs[bug]['failing_tests'][0]['failure_message'].strip(),
                    failing_line=bugs[bug]['failing_tests'][0]['failing_line'].strip()
                ).strip()
            print(prompt)
            print(starting_try)
            # raise Exception("cfp")
            results[bug] = []
            generations = {}
            tries = min(starting_try, 180)
            reset = True
            num_plausible = 1
            while tries < args.total_tries:
                if args.hunk:
                    config = create_chatgpt_config(prev={}, message=prompt+"\n"+PFC_SUFFIX_PROMPT, max_tokens=max_tokens,
                                                   bug_id=bug, bugs=bugs)
                elif args.function:
                    fake_message = [{"role": "system", "content": bugs[bug]['buggy']}]
                    max_tokens = int(num_tokens_from_messages(fake_message) * 1.5)
                    config = create_chatgpt_config(prev={}, message=prompt + "\n" + PFC_SUFFIX_FUNCTION_PROMPT,
                                                   max_tokens=max_tokens,
                                                   bug_id=bug, bugs=bugs)
                else:
                    config = create_chatgpt_config(prev={}, message=prompt + "\n" + PFC_SUFFIX_LINE_PROMPT,
                                                   max_tokens=max_tokens,
                                                   bug_id=bug, bugs=bugs)
                if num_tokens_from_messages(config["messages"]) + max_tokens > 4096:
                    break
                for message in config['messages']:
                    print("{} : {}".format(message['role'], message['content']))
                print("Tries: {} Tokens: {}".format(tries, num_tokens_from_messages(config["messages"])))
                ret = request_chatgpt_engine(config)
                tries += 1
                if args.function:
                    func, pre_history = simple_chatgpt_parse(ret["choices"][0]['message']["content"])
                else:
                    func, pre_history = complex_chatgpt_parse(ret["choices"][0]['message']["content"],
                                                              suffix=bugs[bug]['suffix'],
                                                              prefix=bugs[bug]['prefix'])
                func = remove_comments(func)
                if func != "":
                    if args.function:
                        output = func
                        diff = get_unified_diff(remove_comments(plausible_patch), output)
                    else:
                        output = bugs[bug]['prefix'] + "\n" + func.strip() + "\n" + bugs[bug]['suffix']
                        diff = get_unified_diff(remove_comments(plausible_patch), func.strip())
                    if diff == "":
                        continue
                    print(output)

                    if output.replace(" ", "") not in generations:
                        valid, error_message = write_file(args, args.folder, output,
                                                          bug.split(".java")[0] + "_{}.java".format(
                                                              len(generations)),
                                                          bug.split(".java")[0], skip_val=False, lang=args.lang,
                                                          reset=reset)
                        generations[output.replace(" ", "")] = valid
                        if reset:
                            reset = False
                        if valid:
                            num_plausible += 1
                            if not args.function:
                                prompt += PFC_ADD_PROMPT.format(num=num_plausible, fix_hunk=func.strip())
                    else:
                        valid = generations[output.replace(" ", "")]
                    results[bug].append(
                        {'patch': func, 'valid': valid, "prompt": config,
                         'tries': tries, "usage": ret['usage'], 'output': ret})

        with open(os.path.join(args.folder, "lm_repair.json"), "w") as f:
            json.dump(results, f)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--folder", type=str, default="Results/test")
    parser.add_argument("--target_folder", type=str, default="Results/test")
    parser.add_argument("--dataset", type=str, default="defects4j-1.2-function")
    parser.add_argument("--tmp_prefix", type=str, default="test")
    parser.add_argument("--total_tries", type=int, default=10)
    parser.add_argument("--lang", type=str, default="Java")
    parser.add_argument("--hunk", action="store_true")
    parser.add_argument("--function", default=True)
    parser.add_argument("--key_file", type=str, default="api_key.txt")
    args = parser.parse_args()

    openai.api_key = "sk-wNRQWjcWymP5WY2hDd9bEaE5F66847839cA369E2B77d16Bc"
    os.makedirs(args.folder, exist_ok=True)
    with open(os.path.join(args.folder, "args.txt"), "w") as f:
        f.write(str(args))

    if args.dataset == "quixbugs-python":
        bugs = parse_python("../")
    elif args.dataset == "quixbugs-java":
        bugs = parse_java("../")
    elif args.dataset == "defects4j-1.2-function":
        bugs = parse_defects4j_12("../Dataset/")
    elif args.dataset == "defects4j-1.2-single-hunk":
        bugs = parse_defects4j_12("../Dataset/", single_hunk=True)
    elif args.dataset == "defects4j-1.2-single-line":
        bugs = parse_defects4j_12("../Dataset/", single_line=True)
    elif args.dataset == "defects4j-2.0-single-line":
        bugs = parse_defects4j_2("../Dataset/")
    else:
        raise NotImplementedError

    plausible_to_correct(args, bugs)


if __name__ == "__main__":
    main()
