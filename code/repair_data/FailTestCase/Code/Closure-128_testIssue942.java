  public void testIssue942() {
    assertPrint("var x = {0: 1};", "var x={0:1}");
  }