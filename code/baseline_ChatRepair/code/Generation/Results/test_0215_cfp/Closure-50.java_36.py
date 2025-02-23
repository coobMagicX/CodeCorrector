private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right == null) {
    right = Node.newString(",");
  } else if (!NodeUtil.isImmutableValue(right)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = NodeUtil.getStringValue(right);

  List<Node> arrayFoldedChildren = new LinkedList<Node>();
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
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0); // reset the StringBuilder
      }
      arrayFoldedChildren.add(elem);
      prev = null; // end of continuous string sequence
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
