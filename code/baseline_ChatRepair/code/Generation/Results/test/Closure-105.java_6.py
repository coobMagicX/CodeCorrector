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
  List<Node> arrayFoldedChildren = Lists.newArrayList();
  StringBuilder sb = new StringBuilder();
  int foldedSize = 0;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
      foldedSize += NodeUtil.getStringValue(elem).length() + 2; // +2 for the quotes
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
        // Account for the accumulated string bits
        foldedSize += 2; // since a new string node adds quotes
      }
      arrayFoldedChildren.add(elem.detach());
      foldedSize += InlineCostEstimator.getCost(elem); // Directly using getCost
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
    foldedSize += 2; // account for the quotes added
  }

  // Adding join string costs for intermediate elements
  foldedSize += (arrayFoldedChildren.size() - 1) * joinString.length();
  
  int originalSize = InlineCostEstimator.getCost(n);
  if (foldedSize >= originalSize) {
    return; // No size benefit
  }

  // Construct the possibly optimized configuration back into `arrayNode`
  arrayNode.detachChildren();
  for (Node child : arrayFoldedChildren) {
    arrayNode.addChildToBack(child);
  }
  
  if (arrayFoldedChildren.size() == 1) {
    parent.replaceChild(n, arrayNode.getFirstChild().detach());
  }

  t.getCompiler().reportChangeToEnclosingScope(n);
}
