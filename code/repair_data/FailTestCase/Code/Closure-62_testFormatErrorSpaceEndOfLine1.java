  public void testFormatErrorSpaceEndOfLine1() throws Exception {
    JSError error = JSError.make("javascript/complex.js",
        1, 10, FOO_TYPE);
    LightweightMessageFormatter formatter = formatter("assert (1;");
    assertEquals("javascript/complex.js:1: ERROR - error description here\n" +
        "assert (1;\n" +
        "          ^\n", formatter.formatError(error));
  }