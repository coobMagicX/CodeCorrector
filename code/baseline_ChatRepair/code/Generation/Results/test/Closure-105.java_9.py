void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right, Node parent) {
    if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
        return;
    }

    Node arrayNode = left.getFirstChild();
    Node functionName = arrayNode.getNext();

    if (functionName == null || arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
        return;
    }

    String joinString = NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = Lists.newArrayList();
    StringBuilder sb = new StringBuilder();
    boolean foundNonEmptyString = false;

    Node elem = arrayNode.getFirstChild();
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem)) {
            String string = NodeUtil.getStringValue(elem);
            if (!string.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(joinString);
                }
                sb.append(string);
                foundNonEmptyString = true;
            }
        } else {
            if (sb.length() > 0) {
                arrayFoldedChildren.add(Node.newString(sb.toString()));
                sb.setLength(0);  // clear the StringBuilder
            }
            arrayFoldedChildren.add(elem);
        }
        elem = elem.getNext();
    }

    if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()));
    }

    if (arrayFoldedChildren.isEmpty() && !foundNonEmptyString) {
        // Special case: the array was empty, or all elements were empty strings
        parent.replaceChild(n, Node.newString(""));
        t.getCompiler().reportCodeChange();
        return;
    }

    if (arrayFoldedChildren.size() == 1) {
        parent.replaceChild(n, arrayFoldedChildren.get(0));
        t.getCompiler().reportCodeChange();
        return;
    }

    // Check if the folding resulted in fewer elements
    if (arrayFoldedChildren.size() < arrayNode.getChildCount()) {
        arrayNode.detachChildren();
        for (Node child : arrayFoldedChildren) {
            arrayNode.addChildToBack(child);
        }
        t.getCompiler().reportCodeChange();
    }
}
