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
            Node finallyBlock = NodeUtil.getFinallyBlock(n);
            tryMinimizeExits(finallyBlock, exitType, labelName);
        }
    }

    // Just a 'label'.
    if (n.isLabel()) {
        Node labelBlock = n.getLastChild();
        tryMinimizeExits(labelBlock, exitType, labelName);
    }

    // The rest assumes a block with at least one child, bail on anything else.
    if (!n.isBlock() || n.getFirstChild() == null) {
        return;
    }

    // Multiple if-exits can be converted in a single pass.
    Node c = n.getFirstChild();
    while (c != null) {
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
        c = c.getNext();
    }

    // Now try to minimize the exits of the last child, if it is removed
    // look at what has become the last child.
    Node lastChild = n.getLastChild();
    while (lastChild != null) {
        tryMinimizeExits(lastChild, exitType, labelName);
        if (lastChild == n.getLastChild()) {
            break;
        }
        lastChild = n.getLastChild();
    }
}