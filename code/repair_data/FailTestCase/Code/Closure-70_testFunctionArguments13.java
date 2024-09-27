  public void testFunctionArguments13() throws Exception {
    // verifying that the argument type have non-inferrable types
    testTypes(
        "/** @return {boolean} */ function u() { return true; }" +
        "/** @param {boolean} b\n@return {?boolean} */" +
        "function f(b) { if (u()) { b = null; } return b; }",
        "assignment\n" +
        "found   : null\n" +
        "required: boolean");
  }