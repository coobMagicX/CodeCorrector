  public void testLendsAnnotation3() {
    testSame("/** @constructor */ function F() {}" +
        "dojo.declare(F, /** @lends {F.prototype} */ (" +
        "    {foo: function() { return this.foo; }}));");
  }