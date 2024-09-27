  public void testNoPrivateAccessForProperties8() {
    test(new String[] {
      "/** @constructor */ function Foo() { /** @private */ this.bar_ = 3; }",
      "/** @constructor \n * @extends {Foo} */ " +
      "function SubFoo() { /** @private */ this.bar_ = 3; };"
    }, null, PRIVATE_OVERRIDE);
  }