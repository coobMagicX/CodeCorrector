  public void testDeepNestedBreakwithFinally() {
    String src = "X:while(1){try{while(2){try{var a;break X;}" +
        "finally{}}}finally{}}";
    ControlFlowGraph<Node> cfg = createCfg(src);
    assertDownEdge(cfg, Token.WHILE, Token.BLOCK, Branch.ON_TRUE);
    assertDownEdge(cfg, Token.BLOCK, Token.TRY, Branch.UNCOND);
    assertDownEdge(cfg, Token.BLOCK, Token.VAR, Branch.UNCOND);
    // BREAK to FINALLY.
    assertCrossEdge(cfg, Token.BREAK, Token.BLOCK, Branch.UNCOND);
    // FINALLY to FINALLY.
    assertCrossEdge(cfg, Token.BLOCK, Token.BLOCK, Branch.ON_EX);
    assertCrossEdge(cfg, Token.WHILE, Token.BLOCK, Branch.ON_FALSE);
    assertReturnEdge(cfg, Token.BLOCK);
  }