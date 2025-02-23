private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null) {
    if (!NodeUtil.isImmutableValue(right)) {
      return n; // If not a constant, return unchanged node.
    }
  } else {
    right = Node.newString(","); // Default join string is a comma.
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n; // If not calling join on an array literal, return unchanged node.
  }

  String joinString = NodeUtil.getStringValue(right);

  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (prev != null) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      prev = elem;
    } else {
      if (prev != null) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = new StringBuilder();
        prev = null;
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, foldedStringNode);
    reportCodeChange();
    return foldedStringNode;
  } else {
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    reportCodeChange();
  }

  return n;
}
