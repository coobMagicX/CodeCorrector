  public void testScoping12() throws Exception {
    testTypes(
        "/** @constructor */ function F() {}" +
        "/** @type {number} */ F.prototype.bar = 3;" +
        "/** @param {!F} f */ function g(f) {" +
        "  /** @return {string} */" +
        "  function h() {" +
        "    return f.bar;" +
        "  }" +
        "}",
        "inconsistent return type\n" +
        "found   : number\n" +
        "required: string");
  }