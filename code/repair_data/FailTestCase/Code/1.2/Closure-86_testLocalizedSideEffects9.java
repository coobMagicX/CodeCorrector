  public void testLocalizedSideEffects9() throws Exception {
    // Returning a local object that has been modified
    // is not a global side-effect.
    checkMarkedCalls("/** @constructor A */ function A() {this.x = 1};" +
                     "function f() {" +
                     "  var a = new A; a.foo = 1; return a;" +
                     "}" +
                     "f()",
                     BROKEN_NEW ?
                         ImmutableList.<String>of("A") :
                         ImmutableList.<String>of("A", "f"));
  }