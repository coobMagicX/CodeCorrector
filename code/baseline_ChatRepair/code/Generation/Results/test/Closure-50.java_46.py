private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();
    if (right != null) {
        if (!NodeUtil.isImmutableValue(right)) {
            return n;
        }
    }

    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();

    if (arrayNode.getType() != Token.ARRAYLIT ||
        !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;
    Node elem = arrayNode.getFirstChild();
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
            if (!isFirst) {
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
            isFirst = false;
        } else {
            // If the current element is not immutable and we have built a string,
            // then we need to break and replace the node as handled below.
            if (sb.length() > 0) {
                Node newStringNode = Node.newString(sb.toString()).copyInformationFrom(elem);
                n.getParent().replaceChild(n, newStringNode);
                reportCodeChange();
                return newStringNode;
            }
        }
        elem = elem.getNext();
    }

    if (sb.length() > 0) {
        Node newStringNode = Node.newString(sb.toString()).copyInformationFrom(callTarget);
        n.getParent().replaceChild(n, newStringNode);
        reportCodeChange();
        return newStringNode;
    }

    return n;
}
