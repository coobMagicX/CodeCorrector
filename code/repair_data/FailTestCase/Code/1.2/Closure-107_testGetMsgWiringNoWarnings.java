  public void testGetMsgWiringNoWarnings() throws Exception {
    args.add("--compilation_level=ADVANCED_OPTIMIZATIONS");
    test("/** @desc A bad foo. */ var MSG_FOO = 1;", "");
  }