  public void testCollectedFunctionStubLocal() {
    testSame(
        "(function() {" +
        "/** @constructor */ function f() { " +
        "  /** @return {number} */ this.foo;" +
        "}" +
        "var x = new f();" +
        "});");
    ObjectType x = (ObjectType) findNameType("x", lastLocalScope);
    assertEquals("f", x.toString());
    assertTrue(x.hasProperty("foo"));
    assertEquals("function (this:f): number",
        x.getPropertyType("foo").toString());
    assertFalse(x.isPropertyTypeInferred("foo"));
  }