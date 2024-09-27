# CodeCorrector
*Replication Package of CodeCorrector*

**The replication package for this paper is currently being organized and prepared. We are working to ensure that all necessary components are included and properly documented for reproducibility.**

## Introduction
Automated program repair (APR) plays a crucial role in ensuring the quality of software code, as manual bug fixing is time-consuming and labor-intensive. Despite large language models (LLMs) have gained attention in APR due to their patch generation capabilities, existing APR techniques still suffer from several limitations (e.g., heavy reliance on fault localization, preliminary defect cause analysis, and untargeted global context selection) that might influence program repair. As a result, these LLM-based approaches are effective in some bugs, but struggle to generate correct patches in complex practical scenarios. To address these challenges, a more effective LLM-based APR approach is required to generate more accurate patches. 

In this paper, we propose an APR approach named CodeCorrector, that empowers LLMs’ ability of program repair by supplementing scenario-specific knowledge to mimic developers’ program repair behavior. Given a failing test and its buggy file, CodeCorrector analyzes test objectives and repair logic from test failure information to summarize repair intentions, selects global context information based on repair intentions, then builds repair prompts with scenario-specific knowledge (i.e., repair intents, local and global context) for LLMs to generate patches.Our motivation is to provide a new perspective for automated program repair by following the process of human developers’ manual repair work. 

The evaluation on a subset of the widely-used Defects4J v1.2 and v2.0 datasets shows that overall, CodeCorrector only generates a few patches (i.e., as low as seven) for each defect, but it can outperform all state-of-the-art baselines, and fixes 24 and 25 unique bugs on Defects4J v1.2 and v2.0 respectively. We further analyze the contributions of two core components to the performance of CodeCorrector, especially repair intents improve CodeCorrector by 161% in correct patches and 91% in plausible patches on Defects4J v1.2. Moreover, CodeCorrector improves base LLMs to produce more valid and correct patches by 400% for GPT-3.5 and 270% for GPT-4.

## Dataset
[Defects4J](https://github.com/rjust/defects4j)

## Dependency 
* Ubantu 20.04.6 LTS
* Python 3.7
* Java 1.8
* openai 1.31
* langchain 0.2

## Project Structure
```
.
├── code/                     # Main code and data used for CodeCorrector
│   ├── repair_data/           # Directory containing various information used for code repair
│   │   ├── StructureInfo/     
│   │   ├── SourceCode/        
│   │   ├── RelatedMethods/       
│   │   ├── FailTestCase/
│   │   ├── Defects4j/         # Referenced from [Automated Program Repair in the Era of Large Pre-trained Language Models]
│   │   ├── TestCaseIntent/       
│   │   ├── ChosenMethods/
│   │   └── Generation/    
│   ├── check_ast.py          
│   ├── check_intent.py       
│   ├── check_testcase.py      
│   ├── check_method.py       
│   ├── repair.py              # Main script for performing code repairs
│   └── utils.py               # Utility functions used across the project
├── README.md                  # Project documentation
├── result/                    # Some manual review results
│   ├── 1.2result/             # Results for version 1.2
│   └── 2.0result/             # Results for version 2.0
```

## Result
### Defects4J v1.2

| Project  | #Bug | CodeCorrector    | FitRepair | AlphaRepair | Tare | Selfapr | CURE | GAMMA  | Tbar    |  
|--------- |------|------------------|-----------|-------------|------|---------|------|--------|---------|
| Chart    | 16   | 14               | 8         | 8           | 11   | 7       | 9    | 9/9   | 10      |  
| Closure  | 93   | 19               | 29        | 22          | 22   | 16      | 13   | 20/22 | 18      |  
| Lang     | 42   | 20               | 17        | 11          | 13   | 9       | 9    | 10/17 | 10      |  
| Math     | 72   | 24               | 23        | 19          | 20   | 18      | 9    | 19/25 | 16      |  
| Time     | 16   | 4                | 3         | 3           | 3    | 1       | 1    | 1/2   | 2       |  
| Mockito  | 16   | 5                | 5         | 4           | 4    | 3       | 4    | 2/3   | 2       |  
| **Total**| 255  | **86/122**       | 85  | 67    | 71 | 56 | 52 | 61 | 58 |  

### Defects4J v2.0

| Project          | #Bug | CodeCorrector | FitRepair | AlphaRepair | Tare | Selfapr | CURE | GAMMA | Tbar |
|----------------- |------|---------------|-----------|-------------|------|---------|------|-------|------|
| Closure          | 12   | 1             | 0         | 1           | 1    | 1       | 1    | 1     | 0    |
| Cli              | 23   | 7             | 6         | 7           | 8    | 2       | 5    | 8     | 5    |
| Codec            | 11   | 5             | 5         | 5           | 4    | 6       | 4    | 2     | 2    |
| Collection       | 1    | 0             | 0         | 0           | 1    | 1       | 0    | 0     | 0    |
| Compress         | 33   | 7             | 1         | 6           | 6    | 1       | 4    | 4     | 4    |
| Csv              | 11   | 3             | 1         | 5           | 1    | 1       | 0    | 1     | 0    |
| Gson             | 9    | 3             | 2         | 1           | 1    | 0       | 0    | 1     | 2    |
| JacksonCore      | 13   | 4             | 3         | 3           | 2    | 3       | 2    | 2     | 2    |
| JacksonDatabind  | 51   | 9             | 10        | 8           | 8    | 0       | 9    | 2     | 2    |
| JacksonXml       | 4    | 0             | 1         | 0           | 0    | 0       | 0    | 0     | 0    |
| Jsoup            | 53   | 9             | 9         | 14          | 5    | 5       | 4    | 10    | 9    |
| JxPath           | 7    | 0             | 1         | 0           | 0    | 1       | 2    | 0     | 0    |
| **Total**        | 228  | **51/84**     | 44    | 35      | 38 | 42 | 18 | 38 | 38 |


