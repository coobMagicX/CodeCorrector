private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node arrayNode = callTarget.getFirstChild();
    if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
        return n;
    }

    Node functionName = callTarget.getFirstChild().getNext();
    if (functionName == null || !functionName.getString().equals("join")) {
        return n;
    }

    Node joinArgNode = callTarget.getNext();  // The argument of the join call, if it exists.
    String joinString = (joinArgNode == null) ? "," : NodeUtil.getStringValue(joinArgNode);

    boolean hasOnlyStringLiterals = true;
    StringBuilder joinedString = new StringBuilder();
    Node elem = arrayNode.getFirstChild();
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
            if (joinedString.length() > 0) {
                joinedString.append(joinString);
            }
            joinedString.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            hasOnlyStringLiterals = false;
            break;
        }
        elem = elem.getNext();
    }

    if (hasOnlyStringLiterals) {
        Node newStringNode = Node.newString(joinedString.toString()).srcref(n);
        n.getParent().replaceChild(n, newStringNode);
        reportCodeChange();
        return newStringNode;
    }

    // No change was made because it includes non-literal elements, or other complications.
    return n;
}
