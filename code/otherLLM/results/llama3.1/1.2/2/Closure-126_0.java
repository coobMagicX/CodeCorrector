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
      // Check if the finally block has a break statement, if not then proceed as before.
      if (!matchingExitNode(finallyBlock, exitType, labelName)) {
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
  // Convert "if (blah) break;  if (blah2) break; otherStatement;" into
  // "if (blah) { if (blah2) { break; } } else { otherStatement; }"
  for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
    if (child.isIf() && matchingExitNode(child.getLastChild(), exitType, labelName)) {
      tryMinimizeIfBlockExits(child.getLastChild(), null, child.getParent(), exitType, labelName);
    }
  }

  // Rest of the original code...
}