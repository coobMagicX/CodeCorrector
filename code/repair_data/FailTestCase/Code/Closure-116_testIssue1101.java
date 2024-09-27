  public void testIssue1101() {
    test(
        "var x = (function (saved) {" +
        "    return foo(obj) + saved;" +
        "  })(obj[\"prop\"]);",
        "var x;" +
        "{" +
        "  var saved$$inline_0=obj[\"prop\"];x=foo(obj)+saved$$inline_0" +
        "}");
  }