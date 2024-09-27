  public void testThisTypeOfFunction4() throws Exception {
    testTypes(
        "/** @constructor */ function F() {}" +
        "F.prototype.moveTo = function(x, y) {};" +
        "F.prototype.lineTo = function(x, y) {};" +
        "function demo() {" +
        "  var path = new F();" +
        "  var points = [[1,1], [2,2]];" +
        "  for (var i = 0; i < points.length; i++) {" +
        "    (i == 0 ? path.moveTo : path.lineTo)(" +
        "       points[i][0], points[i][1]);" +
        "  }" +
        "}",
        "\"function (this:F, ?, ?): undefined\" " +
        "must be called with a \"this\" type");
  }