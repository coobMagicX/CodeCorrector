  public void testIssue1002() throws Exception {
    testTypes(
        "/** @interface */" +
        "var I = function() {};" +
        "/** @constructor @implements {I} */" +
        "var A = function() {};" +
        "/** @constructor @implements {I} */" +
        "var B = function() {};" +
        "var f = function() {" +
        "  if (A === B) {" +
        "    new B();" +
        "  }" +
        "};");
  }