  public void testNoInlineIfParametersModified9() {
    // OK, array parameter modified.
    test("function f(a){return a[2]=2}f(o)", "o[2]=2");
  }