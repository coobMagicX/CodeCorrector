  public void testNoInlineIfParametersModified8() {
    // OK, object parameter modified.
    test("function f(a){return a.x=2}f(o)", "o.x=2");
  }