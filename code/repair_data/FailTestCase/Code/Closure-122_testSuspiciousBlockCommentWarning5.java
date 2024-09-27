  public void testSuspiciousBlockCommentWarning5() {
    parse(
        "  /*\n" +
        "   * some random text here\n" +
        "   * @type {number}\n" +
        "   */\n" +
        "  var x = 3;",
        SUSPICIOUS_COMMENT_WARNING);
  }