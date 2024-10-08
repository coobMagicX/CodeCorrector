  public void testIssue586() throws Exception {
    testTypes(
        "/** @constructor */" +
        "var MyClass = function() {};" +
        "/** @param {boolean} success */" +
        "MyClass.prototype.fn = function(success) {};" +
        "MyClass.prototype.test = function() {" +
        "  this.fn();" +
        "  this.fn = function() {};" +
        "};",
        "Function MyClass.prototype.fn: called with 0 argument(s). " +
        "Function requires at least 1 argument(s) " +
        "and no more than 1 argument(s).");
  }