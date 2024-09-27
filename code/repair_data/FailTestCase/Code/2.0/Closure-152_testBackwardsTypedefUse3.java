  public void testBackwardsTypedefUse3() throws Exception {
    testTypes(
        "/** @this {MyTypedef} */ function f() {}" +
        "/** @typedef {(Date|Array)} */ var MyTypedef;",
        "@this type of a function must be an object\n" +
        "Actual type: (Array|Date|null)");
  }