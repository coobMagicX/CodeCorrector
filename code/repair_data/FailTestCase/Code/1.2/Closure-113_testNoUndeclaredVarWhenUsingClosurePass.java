  public void testNoUndeclaredVarWhenUsingClosurePass() {
    enableClosurePass();
    // We don't want to get goog as an undeclared var here.
    test("goog.require('namespace.Class1');\n", null,
        ProcessClosurePrimitives.MISSING_PROVIDE_ERROR);
  }