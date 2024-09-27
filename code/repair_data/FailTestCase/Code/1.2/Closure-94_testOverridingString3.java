  public void testOverridingString3() {
    overrides.put("DEF_OVERRIDE_STRING", Node.newString("foo"));
    test(
        "/** @define {string} */ var DEF_OVERRIDE_STRING = 'x' + 'y';",
        "var DEF_OVERRIDE_STRING=\"foo\"");
  }