  public void testIssue785() {
    inFunction("/** @param {string|{prop: (string|undefined)}} x */" +
               "function f(x) {}" +
               "var out = {};" +
               "f(out);");
    assertEquals("{prop: (string|undefined)}", getType("out").toString());
  }