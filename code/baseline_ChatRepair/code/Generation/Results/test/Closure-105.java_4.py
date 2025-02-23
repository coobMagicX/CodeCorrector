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
  int foldedSize = 0;
  Node elem = arrayNode.getFirstChild();
  // Merges adjacent String nodes.
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
    } else {
      if (sb.length() > 0) {
        // Create a node when there is accumulated string content to add
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
      foldedSize += InlineCostEstimator.getCost(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
  }

  foldedSize += InlineCostEstimator.getJoinCost(arrayFoldedChildren, joinString);

  int originalSize = InlineCostEstimator.getCost(n);
  // Check if any reduction is achieved
  if (foldedSize >= originalSize) {
    return; // Not beneficial to fold
  }

  arrayNode.detachChildren();
  if (arrayFoldedChildren.size() == 1) {
    parent.replaceChild(n, arrayFoldedChildren.get(0));
  } else {
    for (Node foldedNode : arrayFoldedChildren) {
      arrayNode.addChildrenToBack(foldedNode);
    }
    arrayNode.putProp(Node.JOIN_STR_KEY, joinString);
  }
  
  t.getCompiler().reportCodeChange();
}
