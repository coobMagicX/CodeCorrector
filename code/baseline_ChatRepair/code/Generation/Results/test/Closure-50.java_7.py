private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();
    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node functionNameNode = callTarget.getSecondChild();
    Node arrayNode = callTarget.getFirstChild();
    if (arrayNode == null || functionNameNode == null || !functionNameNode.getString().equals("join")) {
        return n;
    }

    // Verify the arrayNode is a literal array and get the separator used in join
    if (arrayNode.getType() != Token.ARRAYLIT) {
        return n;
    }
    Node separatorNode = functionNameNode.getNext();
    String separator = (separatorNode != null && NodeUtil.isString(separatorNode)) ? separatorNode.getString() : "";

    // Build the expression string by concatenating elements with the separator
    StringBuilder exprBuilder = new StringBuilder();
    for (Node child = arrayNode.getFirstChild(); child != null; child = child.getNext()) {
        if (child != arrayNode.getFirstChild()) {
            exprBuilder.append(separator);
        }
        exprBuilder.append(NodeUtil.getArrayElementStringValue(child));
    }

    Node replacementNode = Node.newString(exprBuilder.toString()).srcrefTreeIfMissing(n);

    n.getParent().replaceChild(n, replacementNode);
    reportCodeChange();
    return replacementNode;
}
