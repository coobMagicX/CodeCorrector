private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();
    if (right != null && !NodeUtil.isImmutableValue(right)) {
        return n;
    }

    Node arrayNode = callTarget.getFirstChild();
    if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
        return n;
    }

    Node functionName = callTarget.getSecondChild();
    if (functionName == null || !"join".equals(functionName.getString())) {
        return n;
    }

    // Set the join string, default is ","
    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);

    StringBuilder sb = new StringBuilder();
    for (Node elem = arrayNode.getFirstChild(); elem != null; elem = elem.getNext()) {
        if (NodeUtil.isImmutableValue(elem)) {
            if (sb.length() > 0) {  // Append join string if not the first element
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            // Cannot fold as it contains non-immutable value(s)
            return n;
        }
    }
    // Replace the original node with the new string node
    Node replaceNode = Node.newString(sb.toString()).copyInformationFrom(n);
    n.getParent().replaceChild(n, replaceNode);
    reportCodeChange();
    return replaceNode;
}
