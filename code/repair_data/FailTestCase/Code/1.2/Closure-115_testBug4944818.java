  public void testBug4944818() {
    test(
        "var getDomServices_ = function(self) {\n" +
        "  if (!self.domServices_) {\n" +
        "    self.domServices_ = goog$component$DomServices.get(" +
        "        self.appContext_);\n" +
        "  }\n" +
        "\n" +
        "  return self.domServices_;\n" +
        "};\n" +
        "\n" +
        "var getOwnerWin_ = function(self) {\n" +
        "  return getDomServices_(self).getDomHelper().getWindow();\n" +
        "};\n" +
        "\n" +
        "HangoutStarter.prototype.launchHangout = function() {\n" +
        "  var self = a.b;\n" +
        "  var myUrl = new goog.Uri(getOwnerWin_(self).location.href);\n" +
        "};",
        "HangoutStarter.prototype.launchHangout = function() { " +
        "  var self$$2 = a.b;" +
        "  var JSCompiler_temp_const$$0 = goog.Uri;" +
        "  var JSCompiler_inline_result$$1;" +
        "  {" +
        "  var self$$inline_2 = self$$2;" +
        "  if (!self$$inline_2.domServices_) {" +
        "    self$$inline_2.domServices_ = goog$component$DomServices.get(" +
        "        self$$inline_2.appContext_);" +
        "  }" +
        "  JSCompiler_inline_result$$1=self$$inline_2.domServices_;" +
        "  }" +
        "  var myUrl = new JSCompiler_temp_const$$0(" +
        "      JSCompiler_inline_result$$1.getDomHelper()." +
        "          getWindow().location.href)" +
        "}");
  }