  public void testThisTypeOfFunction3() throws Exception {
    testTypes(
        "/** @constructor */ function F() {}" +
        "F.prototype.bar = function() {};" +
        "var f = (new F()).bar; f();",
        "\"function (this:F): undefined\" must be called with a \"this\" type");
  }