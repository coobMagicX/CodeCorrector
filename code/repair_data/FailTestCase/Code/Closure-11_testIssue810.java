  public void testIssue810() throws Exception {
    testTypes(
        "/** @constructor */" +
        "var Type = function () {" +
        "};" +
        "Type.prototype.doIt = function(obj) {" +
        "  this.prop = obj.unknownProp;" +
        "};",
        "Property unknownProp never defined on obj");
  }