  public void testInlineFunctions6() {
    // more complex inlines
    test("function BAR_FN(x, y, z) { return z(nochg(x + y)) }" +
         "alert(BAR_FN(1, 2, baz))",

         "var JSCompiler_temp_const$$0=alert;" +
         "var JSCompiler_inline_result$$1;" +
         "{JSCompiler_inline_result$$1=baz(nochg(1+2));}" +
         "JSCompiler_temp_const$$0(JSCompiler_inline_result$$1)");
  }