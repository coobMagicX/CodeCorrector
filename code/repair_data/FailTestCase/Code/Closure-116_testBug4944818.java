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
        "HangoutStarter.prototype.launchHangout=function(){" +
        "  var self$$2=a.b;" +
        "  var JSCompiler_temp_const$$0=goog.Uri;" +
        "  var JSCompiler_inline_result$$1;" +
        "  {" +
        "    var JSCompiler_inline_result$$0;" +
        "    {" +
        "      var self$$inline_1=self$$2;" +
        "      if(!self$$inline_1.domServices_) {" +
        "        self$$inline_1.domServices_=goog$component$DomServices.get(self$$inline_1.appContext_);" +
        "      }" +
        "      JSCompiler_inline_result$$0=self$$inline_1.domServices_;" +
        "    }" +
        "    JSCompiler_inline_result$$1=JSCompiler_inline_result$$0.getDomHelper().getWindow();" +
        "  }" +
        "  var myUrl=new JSCompiler_temp_const$$0(JSCompiler_inline_result$$1.location.href)" +
        "}");
  }