  public void testIssue297f() {
    test("function f(a) {" +
         " h((a=1) - (a = g(a)));" +
         "};",
         "function f(a) {" +
         " h((a=1) - (g(a)));" +
         "};");
  }