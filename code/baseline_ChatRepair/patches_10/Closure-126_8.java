void tryMinimizeExits(Node n, int exitType, String labelName) {
  
  if (matchingExitNode(n, exitType, labelName)) {
    NodeUtil.removeChild(n.getParent(), n);
    compiler.reportCodeChange();
    return;
  }

  
  if (n.isIf()) {
    Node ifBlock = n.getFirstChild().getNext();
    tryMinimizeExits(ifBlock, exitType, labelName);
    Node elseBlock = ifBlock.getNext();
    if (elseBlock != null) {
      tryMinimizeExits(elseBlock, exitType, labelName);
    }
    return;
  }

  
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
      Node finallyBlock = NodeUtil.getFinallyBlock(n);
      if (isSafeToOptimizeFinallyBlock(finallyBlock)) {
        tryMinimizeExits(finallyBlock, exitType, labelName);
      }
    }
  }

  
  if (n.isLabel()) {
    Node labelBlock = n.getLastChild();
    tryMinimizeExits(labelBlock, exitType, labelName);
  }

  
  if (!n.isBlock() || n.getLastChild() == null) {
    return;
  }

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


boolean isSafeToOptimizeFinallyBlock(Node finallyBlock) {
  
  return false;
}
