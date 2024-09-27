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
      Node finallyBlock = n.getLastChild();
      tryMinimizeExits(finallyBlock, exitType, labelName);
    }
  }

  // Just a 'label'.
  if (n.isLabel()) {
    Node labelBlock = n.getLastChild();
    tryMinimizeExits(labelBlock, exitType, labelName);
  }

  // The rest assumes a block with at least one child, bail on anything else.
  if (!n.isBlock() || n.getLastChild() == null) {
    return;
  }

  // Multiple if-exits can be converted in a single pass.
  // Convert "if (blah) break;  if (blah2) break; other_stmt;" to
  // become "if (blah); else { if (blah2); else { other_stmt; } }"
  // which will get converted to "if (!blah && !blah2) { other_stmt; }"
  for (Node c : n.children()) {
    if (c.isIf()) {
      Node ifTree = c;
      Node trueBlock = ifTree.getFirstChild().getNext();
      Node falseBlock = trueBlock.getNext();
      tryMinimizeIfBlockExits(trueBlock, falseBlock, ifTree, exitType, labelName);
      trueBlock = ifTree.getFirstChild().getNext();
      falseBlock = trueBlock.getNext();
      if (falseBlock != null) {
        tryMinimizeIfBlockExits(falseBlock, trueBlock, ifTree, exitType, labelName);
      }
    }
    if (c == n.getLastChild()) {
      break;
    }
  }

  for (Node c = n.getLastChild(); c != null; c = n.getLastChild()) {
    tryMinimizeExits(c, exitType, labelName);
    if (c == n.getLastChild()) {
      break;
    }
  }
}

private void tryMinimizeIfBlockExits(Node srcBlock, Node destBlock, Node ifNode, int exitType, String labelName) {
  Node exitNodeParent = null;
  Node exitNode = null;

  if (srcBlock.isBlock()) {
    if (!srcBlock.hasChildren()) {
      return;
    }
    exitNodeParent = srcBlock;
    exitNode = exitNodeParent.getLastChild();
  } else {
    exitNodeParent = ifNode;
    exitNode = srcBlock;
  }

  if (!matchingExitNode(exitNode, exitType, labelName)) {
    return;
  }

  if (ifNode.getNext() != null) {
    Node newDestBlock = IR.block().srcref(ifNode);
    if (destBlock == null) {
      ifNode.addChildToBack(newDestBlock);
    } else if (destBlock.isEmpty()) {
      ifNode.replaceChild(destBlock, newDestBlock);
    } else if (destBlock.isBlock()) {
      newDestBlock = destBlock;
    } else {
      ifNode.replaceChild(destBlock, newDestBlock);
      newDestBlock.addChildToBack(destBlock);
    }

    moveAllFollowing(ifNode, ifNode.getParent(), newDestBlock);
    compiler.reportCodeChange();
  }
}