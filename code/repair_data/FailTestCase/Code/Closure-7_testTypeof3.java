  public void testTypeof3() {
    FlowScope blind = newScope();
    testBinop(blind,
        Token.EQ,
        new Node(Token.TYPEOF, createVar(
            blind, "a", OBJECT_NUMBER_STRING_BOOLEAN)),
        Node.newString("function"),
        Sets.newHashSet(
            new TypedName("a", U2U_CONSTRUCTOR_TYPE)),
        Sets.newHashSet(
            new TypedName("a", OBJECT_NUMBER_STRING_BOOLEAN)));
  }