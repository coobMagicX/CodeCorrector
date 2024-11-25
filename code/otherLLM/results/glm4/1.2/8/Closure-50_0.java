private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null && (!NodeUtil.isImmutableValue(right) || !NodeUtil.isPrimitiveString(right))) {
    return n;
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
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) { // Add join string only if StringBuilder has content
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0); // Clear the StringBuilder for next merge
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) { // Add remaining content in StringBuilder to array FoldedChildren
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
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
        // If the Node is not a string literal, ensure that it is coerced to a string.
        Node replacement = new Node(Token.ADD,
            Node.newString("").copyInformationFrom(n),
            foldedStringNode);
        foldedStringNode = replacement;
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
      foldedSize += (right != null) ? InlineCostEstimator.getCost(right) : 0;
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