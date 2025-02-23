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
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.STRING) {
            String strValue = NodeUtil.getStringValue(elem);
            if (strValue != null) {
                if (sb.length() > 0) {
                    sb.append(joinString);
                }
                sb.append(strValue);
            }
        } else {
            if (sb.length() > 0) {
                foldedSize += sb.length() + 2; // +2 for the quotes
                arrayFoldedChildren.add(Node.newString(sb.toString()));
                sb = new StringBuilder();
            }
            foldedSize += InlineCostEstimator.getCost(elem);
            arrayFoldedChildren.add(elem);
        }
        elem = elem.getNext();
    }

    if (sb.length() > 0) {
        foldedSize += sb.length() + 2; // +2 for the quotes
        arrayFoldedChildren.add(Node.newString(sb.toString()));
    }

    foldedSize += arrayFoldedChildren.size() - 1; // one for each comma

    int originalSize = InlineCostEstimator.getCost(n);

    switch (arrayFoldedChildren.size()) {
        case 0:
            parent.replaceChild(n, Node.newString(""));
            break;

        case 1:
            Node foldedStringNode = arrayFoldedChildren.get(0);
            if (foldedSize > originalSize) {
                return;
            }
            arrayNode.detachChildren();
            parent.replaceChild(n, foldedStringNode);
            break;

        default:
            // Normal folding logic when the size of the optimized output is acceptable
            if (foldedSize + "[].join()".length() + InlineCostEstimator.getCost(right) > originalSize) {
                return;
            }
            arrayNode.detachChildren();
            for (Node node : arrayFoldedChildren) {
                arrayNode.addChildToBack(node);
            }
            break;
    }
    t.getCompiler().reportCodeChange();
}
