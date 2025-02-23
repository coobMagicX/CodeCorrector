void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right, Node parent) {
  if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
    return;
  }

  Node arrayNode = left.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      !functionName.getString().equals("join")) {
    return;
  }

  String joinString = NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder();
  int foldedSize = 0; // Initialize foldedSize to zero
  Node elem = arrayNode.getFirstChild();
  // Merges adjacent String nodes.
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      // Only append join string if string builder is not empty.
      if (sb.length() > 0) {
        sb.append(joinString);
        foldedSize += joinString.length(); // Accumulate the length of join string to foldedSize
      }
      String elemValue = NodeUtil.getStringValue(elem);
      sb.append(elemValue);
      foldedSize += elemValue.length() + 2; // +2 accounting for quotes around the string
    } else {
      if (sb.length() > 0) {
        // Account string built until now and prepare for potential non-string values
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
      foldedSize += InlineCostEstimator.getCost(elem); // Accumulate the cost of non-string elements
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString())); // Add the accumulated final string part
  }

  // Now comparing with original size to decide if folding is advantageous
  int originalSize = InlineCostEstimator.getCost(n);
  if (foldedSize >= originalSize) {
    return; // skip changes if no reduction in size after folding
  }

  // Detach all children to rebuild node structure or just replace old node
  arrayNode.detachChildren();
  if (arrayFoldedChildren.size() == 1) {
    parent.replaceChild(n, arrayFoldedChildren.get(0));
  } else {
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    n.replaceChild(left, arrayNode);
  }
  t.reportChangeToEnclosingScope();
}
