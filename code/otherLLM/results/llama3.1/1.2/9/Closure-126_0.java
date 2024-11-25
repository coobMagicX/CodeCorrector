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
      // Check for a break statement inside the finally block
      if (!finallyBlock.isBreak()) {
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

      // If there's a break statement in the false block
      if (falseBlock.isBreak()) {
        // Don't process it as usual
        continue;
      }

      // Take case of the if nodes siblings, if any.
      if (ifTree.getNext() != null) {
        Node newDestBlock = IR.block().srcref(ifTree);
        if (null == destBlock) {
          // Only possible if this is the false block.
          ifNode.addChildToBack(newDestBlock);
        } else if (destBlock.isEmpty()) {
          // Use the new block.
          ifNode.replaceChild(destBlock, newDestBlock);
        } else if (destBlock.isBlock()) {
          // Reuse the existing block.
          newDestBlock = destBlock;
        } else {
          // Add the existing statement to the new block.
          ifNode.replaceChild(destBlock, newDestBlock);
          newDestBlock.addChildToBack(destBlock);
        }

        // Move all the if node's following siblings.
        moveAllFollowing(ifTree, ifTree.getParent(), newDestBlock);
        compiler.reportCodeChange();
      }
    }
  }
}