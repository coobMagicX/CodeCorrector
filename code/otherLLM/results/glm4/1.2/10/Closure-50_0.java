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
  StringBuilder sb = new StringBuilder(); // Initialize StringBuilder outside of the loop
  int foldedSize = 0;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      String elementStringValue = NodeUtil.getArrayElementStringValue(elem);
      if (sb.length() > 0 && !sb.toString().endsWith(joinString)) {
        sb.append(joinString); // Ensure the separator is only added once between elements
      }
      sb.append(elementStringValue);
    } else {
      if (sb.length() > 0) { // Add the current string buffer to the list before adding non-string elements
        foldedSize += sb.length();
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0); // Clear the StringBuilder for the next group of strings
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) { // Add any remaining elements in the StringBuilder to the list
    foldedSize += sb.length();
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  } else {
    foldedSize += arrayFoldedChildren.size(); // If no strings were added, add cost for each element
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