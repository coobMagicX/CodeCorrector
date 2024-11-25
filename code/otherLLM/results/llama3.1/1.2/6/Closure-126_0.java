void tryMinimizeExits(Node n, int exitType, String labelName) {

  // Just an 'exit'.
  if (matchingExitNode(n, exitType, labelName)) {
    NodeUtil.removeChild(n.getParent(), n);
    compiler.reportCodeChange();
    return;
  }

  // Just an 'if'.
  if (n.isIf()) {
    Node ifBlock = n.getFirstChild().getNext();
    tryMinimizeExits(ifBlock, exitType, labelName);
    Node elseBlock = ifBlock.getNext();
    if (elseBlock != null) {
      tryMinimizeExits(elseBlock, exitType, labelName);
    }
    return;
  }

  // Just a 'try/catch/finally'.
  if (n.isTry()) {
    Node tryBlock = n.getFirstChild();
    tryMinimizeExits(tryBlock, exitType, labelName);
    Node allCatchNodes = NodeUtil.getCatchBlock(n);
    if (NodeUtil.hasCatchHandler(allCatchNodes)) {
      Preconditions.checkState(allCatchNodes.hasOneChild());
      Node catchNode = allCatchNodes.getFirstChild();
      Node catchCodeBlock = catchNode.getLastChild();
      tryMinimizeExits(catchCodeBlock, exitType, labelName);
    }
    /* Don't try to minimize the exits of finally blocks, as this
     * can cause problems if it changes the completion type of the finally
     * block. See ECMA 262 Sections 8.9 & 12.14
     */
    if (NodeUtil.hasFinally(n)) {
      Node finallyBlock = n.getLastChild();
      // Handle break statements correctly in finally block
      tryMinimizeExits(finallyBlock, exitType, labelName);
    }
  }

  // Just a 'label'.
  if (n.isLabel()) {
    Node labelBlock = n.getLastChild();
    tryMinimizeExits(labelBlock, exitType, labelName);
  }

  // Handle the last case of SWITCH statement
  if (n.getType() == Token.SWITCH) {
    Node switchBlock = n.getLastChild();
    for (Node c : switchBlock.children()) {
      if (c.isSwitchCase()) {
        tryMinimizeExits(c, exitType, labelName);
      }
    }
  }

  // The rest assumes a block with at least one child, bail on anything else.
  if (!n.isBlock() || n.getLastChild() == null) {
    return;
  }

  // Multiple if-exits can be converted in a single pass.
  // Convert "if (blah) break;  if (blah2) break; other_stmt;" to
  // become "if (blah); else { if (blah2); else { other_stmt; } }"
  // which will get converted to "if (!blah && !blah2) { other_stmt; }".
  for (Node c : n.children()) {

    // An 'if' block to process below.
    if (c.isIf()) {
      Node ifTree = c;
      Node trueBlock, falseBlock;

      // First, the true condition block.
      trueBlock = ifTree.getFirstChild().getNext();
      falseBlock = trueBlock.getNext();
      tryMinimizeIfBlockExits(trueBlock, falseBlock,
          ifTree, exitType, labelName);

      // Now the else block.
      // The if blocks can be optimized separately
      for (Node child : c.children()) {
        if (child.isIf()) {
          Node innerTrueBlock = child.getFirstChild().getNext();
          Node innerFalseBlock = innerTrueBlock.getNext();
          tryMinimizeIfBlockExits(innerTrueBlock, innerFalseBlock,
              child, exitType, labelName);
        }
      }

      // Now the else block
      falseBlock = c.getLastChild();
      tryMinimizeExits(falseBlock, exitType, labelName);
    }
  }

  for (Node child : n.children()) {
    if (!child.isIf() && !child.isSwitchCase()) {
      tryMinimizeExits(child, exitType, labelName);
    }
  }
}