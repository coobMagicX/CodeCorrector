private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();
    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();

    if ((arrayNode.getType() != Token.ARRAYLIT) ||
        !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = new LinkedList<>();
    StringBuilder sb = new StringBuilder();
    Node prev = null;
    Node elem = arrayNode.getFirstChild();
    
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem)) {
            sb.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            if (sb.length() > 0) {
                arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
                sb.setLength(0);  // reset string builder
            }
            arrayFoldedChildren.add(elem);
        }

        prev = elem;
        elem = elem.getNext();
        if (elem != null && sb.length() > 0) {
            sb.append(joinString);
        }
    }

    if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
    }

    if (arrayFoldedChildren.size() == 1) {
        Node foldedStringNode = arrayFoldedChildren.get(0);
        n.getParent().replaceChild(n, foldedStringNode);
        reportCodeChange();
        return foldedStringNode;
    } else if (!arrayFoldedChildren.equals(arrayNode.children())) {
        arrayNode.detachChildren();
        for (Node node : arrayFoldedChildren) {
            arrayNode.addChildToBack(node);
        }
        reportCodeChange();
    }

    return n;
}

private void reportCodeChange() {
    // This method should notify the compiler that a change has been made.
    // Implement the notification logic here as per the rest of your system.
}
