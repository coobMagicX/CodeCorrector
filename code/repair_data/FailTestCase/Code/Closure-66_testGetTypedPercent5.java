  public void testGetTypedPercent5() throws Exception {
    String js = "/** @enum {number} */ keys = {A: 1,B: 2,C: 3};";
    assertEquals(100.0, getTypedPercent(js), 0.1);
  }