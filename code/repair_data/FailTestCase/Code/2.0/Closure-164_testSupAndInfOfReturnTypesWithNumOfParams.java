  public void testSupAndInfOfReturnTypesWithNumOfParams() {
    FunctionType twoNumbers = new FunctionBuilder(registry)
        .withParamsNode(registry.createParameters(NUMBER_TYPE, NUMBER_TYPE))
        .withReturnType(BOOLEAN_TYPE).build();
    FunctionType oneNumber = new FunctionBuilder(registry)
        .withParamsNode(registry.createParameters(NUMBER_TYPE))
        .withReturnType(BOOLEAN_TYPE).build();

    assertLeastSupertype(
        "function (number, number): boolean", twoNumbers, oneNumber);
    assertGreatestSubtype(
        "function (number): boolean", twoNumbers, oneNumber);
  }