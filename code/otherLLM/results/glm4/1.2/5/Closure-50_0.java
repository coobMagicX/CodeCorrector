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

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb.length() > 0 && !sb.toString().endsWith(",")) { // Ensure the last element is not followed by a comma
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        foldedSize += sb.length(); // Update folded size for previous string concatenation
      }
      arrayFoldedChildren.add(
          Node.newString(sb.toString()).copyInformationFrom(prev));
      sb.setLength(0); // Reset the StringBuilder to start a new string
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
  }

  foldedSize += arrayFoldedChildren.size(); // Add size of children nodes

  int originalSize = InlineCostEstimator.getCost(n);
  switch (arrayFoldedChildren.size()) {
    case 0:
      Node emptyStringNode = Node.newString("");
      n.getParent().replaceChild(n, emptyStringNode);
      reportCodeChange();
      return emptyStringNode;
    case 1:
      Node foldedStringNode = arrayFoldedChildren.get(0);
      if (foldedSize > originalSize) {
        return n;
      }
      arrayNode.detachChildren();
      n.getParent().replaceChild(n, foldedStringNode);
      reportCodeChange();
      return foldedStringNode;
    default:
      // No folding could actually be performed.
      if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
        return n;
      }
      int kJoinOverhead = "[].join()".length();
      foldedSize += kJoinOverhead + (right != null ? InlineCostEstimator.getCost(right) : 0);
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