  public void testIssue700() throws Exception {
    testTypes(
        "/**\n" +
        " * @param {{text: string}} opt_data\n" +
        " * @return {string}\n" +
        " */\n" +
        "function temp1(opt_data) {\n" +
        "  return opt_data.text;\n" +
        "}\n" +
        "\n" +
        "/**\n" +
        " * @param {{activity: (boolean|number|string|null|Object)}} opt_data\n" +
        " * @return {string}\n" +
        " */\n" +
        "function temp2(opt_data) {\n" +
        "  /** @notypecheck */\n" +
        "  function __inner() {\n" +
        "    return temp1(opt_data.activity);\n" +
        "  }\n" +
        "  return __inner();\n" +
        "}\n" +
        "\n" +
        "/**\n" +
        " * @param {{n: number, text: string, b: boolean}} opt_data\n" +
        " * @return {string}\n" +
        " */\n" +
        "function temp3(opt_data) {\n" +
        "  return 'n: ' + opt_data.n + ', t: ' + opt_data.text + '.';\n" +
        "}\n" +
        "\n" +
        "function callee() {\n" +
        "  var output = temp3({\n" +
        "    n: 0,\n" +
        "    text: 'a string',\n" +
        "    b: true\n" +
        "  })\n" +
        "  alert(output);\n" +
        "}\n" +
        "\n" +
        "callee();");
  }