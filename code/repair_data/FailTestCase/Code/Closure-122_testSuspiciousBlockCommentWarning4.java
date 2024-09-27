  public void testSuspiciousBlockCommentWarning4() {
    parse(
        "  /*\n" +
        "   * @type {number}\n" +
        "   */\n" +
        "  var x = 3;",
        SUSPICIOUS_COMMENT_WARNING);
  }