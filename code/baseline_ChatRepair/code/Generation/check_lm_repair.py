import json
import os

newdict = {'patch': None, 'valid': None, "prompt": None, "prompt_times": None,
          'tries': None, "usage": None, "error": None, 'output': None}

folder = "Results/test_0214"
# with open(os.path.join(folder, "lm_repair3.json"), "r") as f:
#     results = json.load(f)
#
# print(type(results))
# print(len(results))
# keys = None
# for n, v in results.items():
#     print(n)
#     if keys is None:
#         keys = v[0].keys()
#     new_dict = {}
#     for key in keys:
#         new_dict[key] = None
#     results[n] = [newdict]
#
# with open(os.path.join(folder, "lm_repair.json"), "w") as f:
#     json.dump(results, f)


# #检查修复结果长度
# with open(os.path.join(folder, "lm_repair.json"), "r") as f:
#     results = json.load(f)
#
# for n, v in results.items():
#     # print(n, ": ", len(v))
#     if len(v) < 10:
#         # raise Exception("stop")
#         print(n, ": ", len(v))


# change model
# folder = "Results"
# with open(os.path.join(folder, "lm_repair.json"), "r") as f:
#     results = json.load(f)
#
# plausible_list = []
# for n, v in results.items():
#     v[:] = [item for item in v if item != newdict]
#     for item in v:
#         if item == newdict:
#             print(item)
#         if item['prompt']['model'] == 'gpt-3.5-turbo':
#             # item['prompt']['model'] = 'gpt-4-turbo'
#             # item['output']['model'] = 'gpt-4-turbo-2024-04-09'
#             print(item['prompt']['model'])
#             print(item['output']['model'])

# with open(os.path.join(folder, "lm_repair.json"), "w") as f:
#     json.dump(results, f)


# 检查修复结果
with open(os.path.join(folder, "lm_repair.json"), "r") as f:
    results = json.load(f)

plausible_list = []
for n, v in results.items():
    # print(n, ": ", len(v))
    for item in v:
        if item['valid']:
            plausible_list.append(n)
            break
        # raise Exception("stop")
# print("plausible_list: ", len(plausible_list), plausible_list)

with open(os.path.join('Results/test', "lm_repair.json"), "r") as f:
    results = json.load(f)

plausible_list_cfp = []
for n, v in results.items():
    # print(n, ": ", len(v))
    for item in v:
        if item['valid']:
            plausible_list_cfp.append(n)
            break
# print("plausible_list: ", len(plausible_list_cfp), plausible_list_cfp)

plausible_list_union = list(set((plausible_list + plausible_list_cfp)))
print("plausible_list_union: ", len(plausible_list_union), plausible_list_union)