private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();
    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node arrayNode = callTarget.getFirstChild();
    if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
        return n;
    }

    Node functionName = arrayNode.getNext();
    if (functionName == null || !functionName.getString().equals("join")) {
        return n;
    }

    Node separatorNode = functionName.getNext();
    // Default separator is "," but if the separator node is present and the value is empty, it should be an empty string.
    String separator = (separatorNode == null || !NodeUtil.isImmutableValue(separatorNode)) ? "," : NodeUtil.getStringValue(separatorNode);

    StringBuilder concatenatedResult = new StringBuilder();
    Node child = arrayNode.getFirstChild();
    boolean isFirst = true;

    while (child != null) {
        if (!isFirst) {
            concatenatedResult.append(separator);
        }
        if (NodeUtil.isImmutableValue(child)) {
            concatenatedResult.append(NodeUtil.getStringLiteralValue(child));
            isFirst = false;
        } else {
            // Non-literal element found, skip folding the join.
            return n;
        }
        child = child.getNext();
    }

    // Replace the original node with the new string.
    Node replacementNode = Node.newString(concatenatedResult.toString()).srcref(n);
    n.replaceWith(replacementNode);
    reportCodeChange();

    return replacementNode;
}
