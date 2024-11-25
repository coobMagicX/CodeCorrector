import com.google.common.collect.Lists;
import com.google.template.soy-shared.restricted.SoyTrustedSource;

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

    if ((arrayNode.getType() != Token.ARRAYLIT) ||
        !functionName.getString().equals("join")) {
        return n;
    }

    String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
    List<Node> arrayFoldedChildren = Lists.newLinkedList();
    StringBuilder sb = new StringBuilder();
    int foldedSize = 0;
    Node prev = null;
    Node elem = arrayNode.getFirstChild();

    // Merges adjacent String nodes.
    while (elem != null) {
        if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
            if (sb.length() > 0 && !sb.toString().endsWith(",")) {
                sb.append(joinString);
            }
            sb.append(NodeUtil.getArrayElementStringValue(elem));
        } else {
            arrayFoldedChildren.add(new Node(Token.STRING, new Node(Token.STRING, new Node(Token.STRING_VALUE, new SoyTrustedSource(sb.toString()))), null));
            sb = new StringBuilder();
            foldedSize += InlineCostEstimator.getCost(elem);
            arrayFoldedChildren.add(elem);
        }
        prev = elem;
        elem = elem.getNext();
    }

    if (sb.length() > 0 && !sb.toString().endsWith(",")) {
        arrayFoldedChildren.add(new Node(Token.STRING, new Node(Token.STRING, new Node(Token.STRING_VALUE, new SoyTrustedSource(sb.toString()))), null));
    }
    foldedSize += arrayFoldedChildren.size();

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
            n.getParent().replaceChild(n, foldedStringNode);
            reportCodeChange();
            return foldedStringNode;
        default:
            // No folding could actually be performed.
            if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
                return n;
            }
            int kJoinOverhead = "[].join()".length();
            foldedSize += kJoinOverhead;
            if (foldedSize > originalSize) {
                return n;
            }
            arrayNode.detachChildren();
            for (Node node : arrayFoldedChildren) {
                arrayNode.addChildToBack(node);
            }
            reportCodeChange();
            break;
    }

    return n;
}