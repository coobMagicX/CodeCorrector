  public void testAssignWithCall() {
    test("var fun, x; (fun = function(){ x; })();",
        "var x; (function(){ x; })();");
  }