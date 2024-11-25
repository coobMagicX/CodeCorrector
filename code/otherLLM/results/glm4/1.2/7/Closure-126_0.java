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
    // Do not minimize exits in the finally block by avoiding direct call to tryMinimizeExits
    if (NodeUtil.hasFinally(n)) {
      Node finallyBlock = n.getLastChild();
      process(new NodeTraversal(compiler, new Node[] {finallyBlock}), finallyBlock);
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
  for (Node c = n.getLastChild(); c != null; c = n.getParent().getLastChild()) {
    process(new NodeTraversal(compiler, new Node[] {c}), c);
    // If the node is still the last child, we are done.
    if (n.getLastChild() == c) {
      break;
    }
  }
}

// Helper method to traverse the tree starting from a given root
private void process(NodeTraversal traversal, Node root) {
  process(traversal, new Node[] {root});
}