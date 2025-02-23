private Node tryFoldArrayJoin(Node n) {
    Node callTarget = n.getFirstChild();

    if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
        return n;
    }

    Node right = callTarget.getNext();  // This is the separator
    Node arrayNode = callTarget.getFirstChild();
    if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
        return n;
    }

    Node functionName = arrayNode.getNext();
    if (functionName == null || !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    boolean hasOnlyStringLiterals = true;
    StringBuilder sb = new StringBuilder();
    Node elem = arrayNode.getFirstChild();
    while (elem != null) {
        if (!NodeUtil.isString(elem)) {
            hasOnlyStringLiterals = false;
            break;
        }
        if (sb.length() > 0) {
            sb.append(joinString);
        }
        sb.append(NodeUtil.getStringValue(elem));
        elem = elem.getNext();
    }

    if (hasOnlyStringLiterals) {
        if (arrayNode.hasOneChild()) {
            // Optimization: If there is only one element and it's a string, just return it directly.
            Node replacementNode = arrayNode.removeFirstChild();
            n.getParent().replaceChild(n, replacementNode);
            reportCodeChange();
            return replacementNode;
        } else {
            Node newStringNode = Node.newString(sb.toString());
            n.getParent().replaceChild(n, newStringNode);
            reportCodeChange();
            return newStringNode;
        }
    }

    // Handles arrays that contain non-string literals or expressions.
    if (right == null || "\"\"".equals(joinString)) {
        // When the join string is empty or default, no need to process further.
        return n;
    }

    // Build a new array join expression using string coercion for non-strings.
    Node addNode = null;
    elem = arrayNode.getFirstChild();
    while (elem != null) {
        Node stringNode;
        if (NodeUtil.isString(elem)) {
            stringNode = elem.cloneTree();
        } else {
            stringNode = new Node(Token.ADD, Node.newString(""), elem.cloneTree());
            stringNode.copyInformationFrom(elem);
        }

        if (addNode == null) {
            addNode = stringNode;
        } else {
            addNode = new Node(Token.ADD, addNode, new Node(Token.ADD, Node.newString(joinString), stringNode));
        }
        elem = elem.getNext();
    }

    if (addNode != null) {
        addNode = NodeUtil.newExpr(addNode);
        n.getParent().replaceChild(n, addNode.getLastChild());
        reportCodeChange();
        return addNode.getLastChild();
    }

    return n;
}
