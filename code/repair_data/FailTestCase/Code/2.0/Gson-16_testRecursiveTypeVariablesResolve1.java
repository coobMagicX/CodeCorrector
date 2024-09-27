  public void testRecursiveTypeVariablesResolve1() throws Exception {
    TypeAdapter<TestType> adapter = new Gson().getAdapter(TestType.class);
    assertNotNull(adapter);
  }