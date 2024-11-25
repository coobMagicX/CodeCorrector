public void visit(NodeTraversal t, Node n, Node parent) {
    switch (n.getType()) {
      case Token.LABEL:
        tryMinimizeExits(
            n.getLastChild(), Token.BREAK, n.getFirstChild().getString());
        break;

      case Token.FOR:
      case Token.WHILE:
        tryMinimizeExits(NodeUtil.getLoopCodeBlock(n), Token.CONTINUE, null);
        break;

      case Token.DO:
        tryMinimizeExits(NodeUtil.getLoopCodeBlock(n), Token.CONTINUE, null);

        Node cond = NodeUtil.getConditionExpression(n);
        if (NodeUtil.getImpureBooleanValue(cond) == TernaryValue.FALSE) {
          // Normally, we wouldn't be able to optimize BREAKs inside a loop
          // but as we know the condition will always false, we can treat them
          // as we would a CONTINUE.
          tryMinimizeExits(n.getFirstChild(), Token.BREAK, null);
        }
        break;

      case Token.FUNCTION:
        tryMinimizeExits(n.getLastChild(), Token.RETURN, null);
        break;

      case Token.TRY:
        Node tryBlock = n.getFirstChild();
        Node catchBlock = n.getSecondChild();
        if (catchBlock != null && NodeUtil.hasFinally(catchBlock)) {
          // When there is a finally block in the catch block, we need to minimize the exits in both
          // the try and catch blocks separately.
          tryMinimizeExits(tryBlock, Token.RETURN, null);
          tryMinimizeExits(catchBlock, Token.RETURN, null);
        } else {
          // If there is no finally block, we can minimize the exits only in the try block
          tryMinimizeExits(tryBlock, Token.RETURN, null);
        }
        break;
    }
}