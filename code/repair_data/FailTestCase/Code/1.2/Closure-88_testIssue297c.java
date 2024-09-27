  public void testIssue297c() {
    test("function f() {" +
         " var x;" +
         " return (x=1) && (x = f(x));" +
         "};",
         "function f() {" +
         " var x;" +
         " return (x=1) && f(x);" +
         "};");
  }