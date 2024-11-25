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
    // Do not minimize exits of finally blocks to preserve control flow.
    if (NodeUtil.hasFinally(n)) {
      Node finallyBlock = n.getLastChild();
      // Check for break statements within the finally block
      // and do not remove them unless it's a structured exit point
      if (!containsBreak(finallyBlock)) {
        tryMinimizeExits(finallyBlock, exitType, labelName);
      }
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

  // Now try to minimize the exits of the last child, if it is removed
  // look at what has become the last child.
  for (Node c = n.getLastChild(); c != null; c = n.getLastChild()) {
    tryMinimizeExits(c, exitType, labelName);
    // If the node is still the last child, we are done.
    if (c == n.getLastChild()) {
      break;
    }
  }
}

// Method to check for 'break' statements within a block
private boolean containsBreak(Node block) {
  for (Node child : block.children()) {
    if ("break".equals(child.getName())) {
      return true;
    }
  }
  return false;
}