  public void testPropertyOfMethod() {
    testFailure("a.protoype.b = {}; " +
        "a.prototype.b.c = function() { this.foo = 3; };");
  }