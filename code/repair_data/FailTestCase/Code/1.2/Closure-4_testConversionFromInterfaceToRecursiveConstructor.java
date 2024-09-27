  public void testConversionFromInterfaceToRecursiveConstructor()
      throws Exception {
    testClosureTypesMultipleWarnings(
        suppressMissingProperty("foo") +
            "/** @interface */ var OtherType = function() {}\n" +
            "/** @implements {MyType} \n * @constructor */\n" +
            "var MyType = function() {}\n" +
            "/** @type {MyType} */\n" +
            "var x = /** @type {!OtherType} */ (new Object());",
        Lists.newArrayList(
            "Parse error. Cycle detected in inheritance chain of type MyType",
            "initializing variable\n" +
            "found   : OtherType\n" +
            "required: (MyType|null)"));
  }