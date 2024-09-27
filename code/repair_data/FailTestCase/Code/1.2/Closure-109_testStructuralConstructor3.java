  public void testStructuralConstructor3() throws Exception {
    resolve(parse("@type {function (new:*)} */").getType(),
        "constructed type must be an object type");
  }