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
    // Check if there is a finally block and it's not empty
    if (NodeUtil.hasFinally(n)) {
        Node finallyBlock = n.getFinallyBlock();
        if (!finallyBlock.isEmpty()) {
            // Try to minimize exits in the finally block
            tryMinimizeExits(finallyBlock, exitType, labelName);
        }
    }
}