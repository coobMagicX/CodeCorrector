  public void testIssue297d() {
    test("function f(a) {" +
         " return (a=1) && (a = f(a));" +
         "};",
         "function f(a) {" +
         " return (a=1) && (f(a));" +
         "};");
  }