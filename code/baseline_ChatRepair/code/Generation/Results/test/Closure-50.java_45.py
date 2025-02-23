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

    Node functionNameNode = arrayNode.getNext();
    if (functionNameNode == null || !"join".equals(functionNameNode.getString())) {
        return n;
    }

    // Determining the join string, letting ',' to be default.
    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);

    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (Node elem = arrayNode.getFirstChild(); elem != null; elem = elem.getNext()) {
        if (NodeUtil.isImmutableValue(elem)) {
            if (!first) {
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
            first = false;
        } else {
            // This ensures that we only fold if all elements are immutable.
            return n;
        }
    }

    // Replace the original .join() call with the resulting String node.
    Node replaceNode = Node.newString(sb.toString()).copyInformationFrom(n);
    n.getParent().replaceChild(n, replaceNode);
    reportCodeChange();
    return replaceNode;
}
