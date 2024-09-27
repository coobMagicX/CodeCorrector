  public void testBackwardsTypedefUse2() throws Exception {
    testTypes(
        "/** @this {MyTypedef} */ function f() {}" +
        "/** @typedef {!(Date|Array)} */ var MyTypedef;");
  }