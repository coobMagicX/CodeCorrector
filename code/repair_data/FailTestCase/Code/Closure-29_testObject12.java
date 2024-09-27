  public void testObject12() {
    testLocal("var a; a = {x:1, y:2}; f(a.x, a.y2);",
        "var a; a = {x:1, y:2}; f(a.x, a.y2);");
  }