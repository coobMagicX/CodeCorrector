  public void testSyntaxError2() {
    try {
      extractMessage("", "if (true) {}}");
      fail("Expected exception");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("JSCompiler errors\n"));
      assertTrue(e.getMessage().contains(
          "testcode:2: ERROR - Parse error. syntax error\n"));
      assertTrue(e.getMessage().contains("if (true) {}}\n"));