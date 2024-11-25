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
    if (NodeUtil.hasFinally(n)) {
      // Preserve break statements in the finally block
      Node finallyBlock = n.getLastChild();
      for (Node child : finallyBlock.children()) {
        if (child.getType() == Token.BREAK) {
          // If a 'break' statement is found, do not minimize this block
          return;
        }
      }
      tryMinimizeExits(finallyBlock, exitType, labelName);
    }
  }

  // Just a 'label'.
  if (n.isLabel()) {
    Node labelBlock = n.getLastChild();
    tryMinimizeExits(labelBlock, exitType, labelName);
  }

  // TODO(johnlenz): The last case of SWITCH statement?

  // The rest assumes a block with at least one child, bail on anything else.
  if (!n.isBlock() || n.getLastChild() == null) {
    return;
  }

  // Multiple if-exits can be converted in a single pass.
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
      // The if blocks may have changed, get them again.
      trueBlock = ifTree.getFirstChild().getNext();
      falseBlock = trueBlock.getNext();
      if (falseBlock != null) {
        tryMinimizeIfBlockExits(falseBlock, trueBlock,
            ifTree, exitType, labelName);
      }
    }

    if (c == n.getLastChild()) {
      break;
    }
  }

  // Now try to minimize the block itself
  for (Node child : n.children()) {
    if (!child.isBreak() && !child.isContinue() && !child.isReturn()) {
      // Visit each child node
      visit(new NodeTraversal(compiler, this), child, n);
    }
  }
}