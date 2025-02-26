# CodeCorrector
*Replication Package of CodeCorrector*

**The replication package for this paper is currently being organized and prepared. We are working to ensure that all necessary components are included and properly documented for reproducibility.**

## Introduction
Automated program repair (APR) plays a crucial role in ensuring the quality of software code, as manual bug fixing is time-consuming and labor-intensive. Despite large language models (LLMs) have gained attention in APR due to their patch generation capabilities, existing APR techniques still suffer from several limitations (e.g., heavy reliance on fine-grained fault localization and untargeted context selection) that might influence program repair. As a result, these LLM-based approaches are effective in some bugs, but struggle to generate correct patches in complex practical scenarios. To address these challenges, a more effective LLM-based APR approach is required to generate more accurate patches. 

In this paper, we propose an APR approach named CodeCorrector, which empowers the bug-fixing ability of LLMs by context-aware prompting that adaptively selects the specific context for each defect. Given a failing test and its buggy file, CodeCorrector analyzes test objectives from failing tests and deduces implicit repair logic to distill repair intentions, selects global context information according to repair intentions, then builds context-aware repair prompts with specific scenario information (i.e., repair intents, local and global context) for LLMs to generate patches.  Our motivation is to provide a new perspective for more powerful LLM-based program repair by analyzing the traits (e.g., repair intent) of each bug and adaptively selecting its relevant and useful context to effectively guide LLMs for correct program repair in complex code scenarios. 

Our evaluation on the widely-used Defects4J (i.e., v1.2 and v2.0) benchmark shows that (1) CodeCorrector outperforms all the state-of-the-art baselines, especially on the more complex defects in Defects4J v2.0 and the defects without fine-grained (e.g., line-level) defect localization information in Defects4J v1.2. Specifically, by generating just a small number of patches (i.e., as few as ten rounds for repair), CodeCorrector fixes 34.1% (i.e., 87 out of 255) and 27.6% (i.e., 63 out of 228) of bugs on Defects4J v1.2 and v2.0 respectively, among which 38 defects were correctly fixed only by CodeCorrector in total. (2) We conducted an ablation experiment to confirm the contribution of the two core components in CodeCorrector (i.e., repair directions, global context selection). Particularly, the component of repair directions achieves an improvement of 112% in generating correct patches on Defects4J v1.2. (3) Compared with the base LLMs (i.e., gpt-3.5-turbo and gpt-4-turbo), CodeCorrector achieves the improvement of 377% and 268% in generating correct patches respectively. Overall, our experimental results demonstrate the effectiveness of CodeCorrector, opening the new insight of boosting LLMs with context-aware prompting for more powerful APR.


## Dataset
### [Defects4J](https://github.com/rjust/defects4j)
### [QuixBugs](https://github.com/jkoppel/QuixBugs)

## Dependency 
* Ubantu 20.04.6 LTS
* Python 3.7
* Java 1.8
* openai 1.31
* langchain 0.2
* Ollama 0.3.3

## Project Structure
```
.
├── code/                     # Main code and data used for CodeCorrector
│   ├── repair_data/          # Directory containing various information used for code repair
│   ├── otherLLM/             # Updated experiments on open source small parameter models
│   │   ├── StructureInfo/     
│   │   ├── SourceCode/        
│   │   ├── RelatedMethods/       
│   │   ├── FailTestCase/
│   │   ├── Defects4j/         # Referenced from [Automated Program Repair in the Era of Large Pre-trained Language Models]
│   │   ├── TestCaseDirection/       
│   │   ├── ChosenMethods/
│   │   └── Generation/    
│   ├── check_ast.py          
│   ├── check_direction.py       
│   ├── check_testcase.py      
│   ├── check_method.py       
│   ├── repair.py              # Main script for performing code repairs
│   └── utils.py               # Utility functions used across the project
├── README.md                  # Project documentation
├── baseline_ChatRepair        # The baseline code we reproduce
├── result/                    # Some manual review results
│   ├── 1.2result/             # Results for version 1.2
│   ├── 2.0result/             # Results for version 2.0
│   └── quixbugs_result/       # Results for quixbugs_result
```

## Code
You can modify and run the code in the following way:
1. Modify the file paths in the code to match your actual setup.
2. Run check_testcase.py to extract testcase and assert statement.
```
python check_testcase.py
```
3. Run check_direction.py to infer repair direction.
```
python check_direction.py
```
4. Run check_ast.py to construct candidate repository.
```
python check_ast.py
```
5. Run check_method.py to select context.
```
python check_method.py
```
6. Run repair.py to fix the bug.
```
python repair.py
```

## Result
### Defects4J v1.2

| Project   | #Bug | CodeCorrector | ChatRepair | FitRepair | AlphaRepair | Tare | SelfApr | CURE | GAMMA     | Tbar |
|-----------|------|---------------|------------|-----------|-------------|------|---------|------|-----------|------|
| Chart     | 16   | 14            | 13         | 8         | 8           | 11   | 7       | 9    | 9/9       | 10   |
| Closure   | 93   | 20            | 25(37)     | 29        | 22          | 22   | 16      | 13   | 20/22     | 18   |
| Lang      | 42   | 20            | 13(21)     | 17        | 11          | 13   | 9       | 9    | 10/17     | 10   |
| Math      | 72   | 24            | 17(32)     | 23        | 23          | 20   | 18      | 19   | 19/25     | 16   |
| Time      | 16   | 4             | 2(3)       | 3         | 3           | 3    | 1       | 1    | 1/2       | 2    |
| Mockito   | 16   | 5             | 6          | 5         | 4           | 5    | 4       | 4    | 2/3       | 2    |
| **Total** | 255  | **87/128**        | 76(114)| 85    | 67      | 71| 56  | 52| 61    | 58|



### Defects4J v2.0

| Project          | #Bug | CodeCorrector | ChatRepair | FitRepair | AlphaRepair | Tare | SelfApr | CURE | GAMMA | Tbar |
|-------------------|------|---------------|------------|-----------|-------------|------|---------|------|-------|------|
| Closure          | 12   | 1             | 1          | 0         | 1           | 1    | 1       | 1    | 1     | 0    |
| Cli              | 23   | 7             | 6          | 6         | 7           | 8    | 2       | 5    | 8     | 5    |
| Codec            | 11   | 5             | 5          | 5         | 5           | 6    | 1       | 4    | 2     | 2    |
| Collection       | 3    | 1             | 1          | 1         | 1           | 1    | 0       | 1    | 1     | 1    |
| Compress         | 3    | 0             | 0          | 0         | 0           | 0    | 0       | 0    | 0     | 0    |
| Csv              | 4    | 1             | 1          | 0         | 0           | 1    | 0       | 0    | 0     | 0    |
| Gson             | 3    | 1             | 1          | 0         | 0           | 1    | 0       | 0    | 0     | 0    |
| JacksonCore      | 13   | 4             | 3          | 3         | 3           | 4    | 1       | 4    | 4     | 1    |
| JacksonDatabind  | 51   | 10            | 10         | 8         | 8           | 10   | 2       | 8    | 9     | 5    |
| JacksonXml       | 4    | 1             | 1          | 0         | 1           | 1    | 0       | 0    | 1     | 0    |
| Jsoup            | 53   | 9             | 9          | 9         | 14          | 9    | 1       | 10   | 10    | 9    |
| JxPath           | 7    | 0             | 0          | 0         | 0           | 0    | 0       | 0    | 0     | 0    |
| **Total**        | 228  | **63/100**        | 48     | 44    | 35      | 42| 18  | 38| 38| 18|

### QuixBugs

| Dataset          | #Bug | CodeCorrector | ChatRepair | AlphaRepair | Tare | CURE | GAMMA | CoCoNut |
|------------------|------|---------------|------------|-------------|------|------|-------|---------|
| QuixBugs_Java    | 40   | 30/35         | 40/-       | 28/30       | 27/27| 26/35| 22/-  | 13/20   |
| QuixBugs_Python  | 40   | 38/39         | 40/-       | 27/32       | -/-  | -/-  | -/-   | 19/21   |
