  public void testImplementsLoop() throws Exception {
    testClosureTypesMultipleWarnings(
        suppressMissingProperty("foo") +
        "/** @constructor \n * @implements {T} */var T = function() {};" +
        "alert((new T).foo);",
        Lists.newArrayList(
            "Parse error. Cycle detected in inheritance chain of type T"));
  }