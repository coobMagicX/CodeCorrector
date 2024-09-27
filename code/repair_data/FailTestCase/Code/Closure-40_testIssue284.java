  public void testIssue284() {
    test(
        "var goog = {};" +
        "goog.inherits = function(x, y) {};" +
        "var ns = {};" +
        "/** @constructor */" +
        "ns.PageSelectionModel = function() {};" +
        "/** @constructor */" +
        "ns.PageSelectionModel.FooEvent = function() {};" +
        "/** @constructor */" +
        "ns.PageSelectionModel.SelectEvent = function() {};" +
        "goog.inherits(ns.PageSelectionModel.ChangeEvent," +
        "    ns.PageSelectionModel.FooEvent);",
        "");
  }