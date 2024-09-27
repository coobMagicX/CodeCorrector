  public void testOverridingString1() {
    test(
        "/** @define {string} */ var DEF_OVERRIDE_STRING = 'x' + 'y';",
        "var DEF_OVERRIDE_STRING=\"x\" + \"y\"");
  }  