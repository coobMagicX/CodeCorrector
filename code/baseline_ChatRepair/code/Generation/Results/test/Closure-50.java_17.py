private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();
    Node arrayNode = callTarget.getFirstChild();
    Node functionName = arrayNode.getNext();

    if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = new LinkedList<Node>();
    StringBuilder sb = new StringBuilder();
    Node prev = null;
    Node elem = arrayNode.getFirstChild();
    
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem)) {
            if (sb.length() > 0) {
                // Append join string only if there's already something in StringBuilder
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            if (sb.length() > 0) {
                arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
                sb.setLength(0);  // Reset StringBuilder
            }
            arrayFoldedChildren.add(elem);
        }

        prev = elem;
        elem = elem.getNext();
    }

    // Append any final string left in StringBuilder
    if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
    }

    // Result handling
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
    // This method should notify the compiler that a change has been made based on your actual system setup.
    // Implement the notification logic here.
}
