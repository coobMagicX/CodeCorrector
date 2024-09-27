  public void testRemoveGlobal1() {
    removeGlobal = false;
    testSame("var x=1");
    test("var y=function(x){var z;}", "var y=function(x){}");
  }