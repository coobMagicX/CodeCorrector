  public void testGetTypedPercent6() throws Exception {
    String js = "a = {TRUE: 1, FALSE: 0};";
    assertEquals(100.0, getTypedPercent(js), 0.1);
  }