  public void testFormatErrorSpaceEndOfLine2() throws Exception {
    JSError error = JSError.make("javascript/complex.js",
        6, 7, FOO_TYPE);
    LightweightMessageFormatter formatter = formatter("if (foo");
    assertEquals("javascript/complex.js:6: ERROR - error description here\n" +
        "if (foo\n" +
        "       ^\n", formatter.formatError(error));
  }