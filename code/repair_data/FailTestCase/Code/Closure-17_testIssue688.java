  public void testIssue688() throws Exception {
    testTypes(
        "/** @const */ var SOME_DEFAULT =\n" +
        "    /** @type {TwoNumbers} */ ({first: 1, second: 2});\n" +
        "/**\n" +
        "* Class defining an interface with two numbers.\n" +
        "* @interface\n" +
        "*/\n" +
        "function TwoNumbers() {}\n" +
        "/** @type number */\n" +
        "TwoNumbers.prototype.first;\n" +
        "/** @type number */\n" +
        "TwoNumbers.prototype.second;\n" +
        "/** @return {number} */ function f() { return SOME_DEFAULT; }",
        "inconsistent return type\n" +
        "found   : (TwoNumbers|null)\n" +
        "required: number");
  }