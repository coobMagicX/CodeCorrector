void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right, Node parent) {
    if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
        return;
    }

    Node arrayNode = left.getFirstChild();
    Node functionName = arrayNode.getNext();

    if ((arrayNode.getType() != Token.ARRAYLIT) ||
        !functionName.getString().equals("join")) {
        return;
    }

    String joinString = NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = Lists.newLinkedList();
    StringBuilder sb = new StringBuilder();
    int foldedSize = 0;
    Node elem = arrayNode.getFirstChild();

    // Merges adjacent String nodes.
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem)) {
            if (sb.length() > 0) {
                sb.append(joinString);
            }
            sb.append(NodeUtil.getStringValue(elem));
        } else {
            if (sb.length() > 0) {
                foldedSize += sb.length() + 2;  // quotes
                arrayFoldedChildren.add(Node.newString(sb.toString()));
                sb = new StringBuilder();
            }
            if (NodeUtil.isImmutableValue(elem) || elem instanceof Node.StringNode) {
                sb.append(NodeUtil.getStringValue(elem));
            } else {
                // Process a non-string, non-immutable node:
                // Stop appending to sb and add this node to arrayFoldedChildren.
                foldedSize += InlineCostEstimator.getCost(elem);
                arrayFoldedChildren.add(elem);
            }
        }
        elem = elem.getNext();
    }

    // Append any remaining contents of sb.
    if (sb.length() > 0) {
        foldedSize += sb.length() + 2;  // quotes
        arrayFoldedChildren.add(Node.newString(sb.toString()));
    }

    // Sum the sizes, accounting for commas in the join.
    foldedSize += arrayFoldedChildren.size() - 1;

    int originalSize = InlineCostEstimator.getCost(n);
    if (foldedSize >= originalSize) {
        return;  // Not beneficial to replace.
    }

    switch (arrayFoldedChildren.size()) {
        case 0:
            parent.replaceChild(n, Node.newString(""));
            break;
        case 1:
            Node singleNodeResult = arrayFoldedChildren.get(0);
            if (singleNodeResult.isString()) {
                parent.replaceChild(n, singleNodeResult);
            } else {
                Node castNode = new Node(Token.ADD, Node.newString(""), singleNodeResult.cloneTree());
                parent.replaceChild(n, castNode);
            }
            break;
        default:
            arrayNode.detachChildren();
            for (Node childNode : arrayFoldedChildren) {
                if (!childNode.isString()) {
                    // Ensure childNode is cast to string:
                    childNode = new Node(Token.ADD, Node.newString(""), childNode.cloneTree());
                }
                arrayNode.addChildToBack(childNode);
            }
            break;
    }
    t.getCompiler().reportCodeChange();
}
