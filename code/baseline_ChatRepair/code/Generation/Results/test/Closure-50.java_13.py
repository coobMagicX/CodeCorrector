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
  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (sb.length() > 0 && (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY)) {
      sb.append(joinString);  // Append join string when needed.
    }
    if (NodeUtil.isStringLiteral(elem)) {
      sb.append("\"").append(NodeUtil.getStringLiteralValue(elem)).append("\"");
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = new StringBuilder();  // Reset after adding to children.
      }
      arrayFoldedChildren.add(elem);  // Directly add non-literal
    }
    prev = elem;
    elem = elem.getNext();
  }
  if (sb.length() > 0) {  // If buffer has leftover content
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.isEmpty()) {
    Node emptyStringNode = Node.newString("");
    n.getParent().replaceChild(n, emptyStringNode);
    reportCodeChange();
    return emptyStringNode;
  } else if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, foldedStringNode);
    reportCodeChange();
    return foldedStringNode;
  } else {
    // Reconstruct the folded arrayNode
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    reportCodeChange();
  }
  return n;
}
