  public void testTemplatized11() throws Exception {
    testTypes(
        "/** \n" +
        " * @template T\n" +
        " * @constructor\n" +
        " */\n" +
        "function C() {}\n" +
        "\n" +
        "/**\n" +
        " * @param {T|K} a\n" +
        " * @return {T}\n" +
        " * @template K\n" +
        " */\n" +
        "C.prototype.method = function (a) {};\n" +
        "\n" +
        // method returns "?"
        "/** @type {void} */ var x = new C().method(1);");
  }