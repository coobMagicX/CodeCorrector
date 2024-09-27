  public void testIssue931() {
    collapsePropertiesOnExternTypes = true;
    testSame(
      "function f() {\n" +
      "  return function () {\n" +
      "    var args = arguments;\n" +
      "    setTimeout(function() { alert(args); }, 0);\n" +
      "  }\n" +
      "};\n");
  }