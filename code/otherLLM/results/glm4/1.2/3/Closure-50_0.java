private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null && (!NodeUtil.isImmutableValue(right) || right.getType() == Token.STRING)) {
    // Only proceed if the value is immutable and not already a string
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      !functionName.getString().equals("join")) {
    return n;
  }

  // "," is the default, it doesn't need to be explicit

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder(); // Initialize sb here
  int foldedSize = 0;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb.length() > 0 && !NodeUtil.getStringValue(elem).startsWith("\"") && !NodeUtil.getStringValue(elem).endsWith("\"")) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) { // If sb is not empty, we need to add it to the list
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0); // Reset StringBuilder after adding to the list
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) { // Add the final content of sb to the list if it's not empty
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