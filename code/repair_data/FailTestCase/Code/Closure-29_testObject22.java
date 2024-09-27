  public void testObject22() {
    testLocal("while(1) { var a = {y:1}; if (b) a.x = 2; f(a.y, a.x);}",
      "for(;1;){" +
      " var JSCompiler_object_inline_y_0=1;" +
      " var JSCompiler_object_inline_x_1;" +
      " if(b) JSCompiler_object_inline_x_1=2;" +
      " f(JSCompiler_object_inline_y_0,JSCompiler_object_inline_x_1)" +
      "}");

    testLocal("var a; while (1) { f(a.x, a.y); a = {x:1, y:1};}",
        "var a; while (1) { f(a.x, a.y); a = {x:1, y:1};}");
  }