import com.google.common.collect.Lists;

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
    Node functionName = arrayNode.getNext();

    if ((arrayNode.getType() != Token.ARRAYLIT) ||
        !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    StringBuilder sb = new StringBuilder();
    List<Node> arrayFoldedChildren = Lists.newLinkedList();
    Node prev = null;
    Node elem = arrayNode.getFirstChild();

    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
            String stringValue = NodeUtil.getArrayElementStringValue(elem);
            if (sb.length() > 0 && !stringValue.isEmpty()) {
                sb.append(joinString).append(stringValue);
            } else {
                sb.append(stringValue);
            }
        } else {
            arrayFoldedChildren.add(elem);
            // Reset StringBuilder after a non-string or empty string element
            sb.setLength(0);
        }
        prev = elem;
        elem = elem.getNext();
    }

    if (sb.length() > 0) { // Add the last value without joinString
        arrayFoldedChildren.add(Node.newString(sb.toString()));
    } else if (!arrayFoldedChildren.isEmpty()) {
        sb.append(arrayFoldedChildren.remove(0).toString());
        arrayFoldedChildren.add(0, NodeUtil.createStringFromNode(sb));
    }

    int originalSize = InlineCostEstimator.getCost(n);
    switch (arrayFoldedChildren.size()) {
        case 0:
            Node emptyStringNode = Node.newString("");
            n.getParent().replaceChild(n, emptyStringNode);
            reportCodeChange();
            return emptyStringNode;
        case 1:
            Node foldedStringNode = arrayFoldedChildren.remove(0);
            if (foldedSize > originalSize) {
                return n;
            }
            arrayNode.detachChildren();
            if (foldedStringNode.getType() != Token.STRING) {
                Node replacement = new Node(Token.ADD,
                    Node.newString("").copyInformationFrom(n),
                    foldedStringNode);
                foldedStringNode = replacement;
            }
            n.getParent().replaceChild(n, foldedStringNode);
            reportCodeChange();
            return foldedStringNode;
        default:
            arrayNode.detachChildren();
            for (Node node : arrayFoldedChildren) {
                arrayNode.addChildToBack(node);
            }
            reportCodeChange();
            break;
    }

    return n;
}