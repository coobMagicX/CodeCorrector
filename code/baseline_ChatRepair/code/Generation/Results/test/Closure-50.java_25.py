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
  if (arrayNode.getType() != Token.ARRAYLIT || !"join".equals(functionName.getString())) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  StringBuilder sb = new StringBuilder();
  ArrayList<Node> arrayFoldedChildren = new ArrayList<>();
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(elem));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(callTarget));
  }

  // Rebuild array or just return if no changes made
  if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
    return n;
  }

  // Replacement node construction
  arrayNode.detachChildren();
  for (Node node : arrayFoldedChildren) {
    arrayNode.addChildToBack(node);
  }

  reportCodeChange();
  return n;
}
