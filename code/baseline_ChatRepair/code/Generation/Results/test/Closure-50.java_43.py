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
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  int foldedSize = 0;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  Node firstNonLiteral = null;
  
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (firstNonLiteral != null) {
        foldedSize += InlineCostEstimator.getCost(firstNonLiteral);
        arrayFoldedChildren.add(firstNonLiteral);
        firstNonLiteral = null;
      }
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        Preconditions.checkNotNull(prev);
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        foldedSize += sb.length() + 2; // +2 for the quotes
        sb.setLength(0); // reset StringBuilder
      }
      if (firstNonLiteral == null) {
        firstNonLiteral = elem;
      }
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (firstNonLiteral != null) {
    foldedSize += InlineCostEstimator.getCost(firstNonLiteral);
    arrayFoldedChildren.add(firstNonLiteral);
  }
  if (sb.length() > 0) {
    Preconditions.checkNotNull(prev);
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
    foldedSize += sb.length() + 2; // +2 for the quotes
  }

  foldedSize += arrayFoldedChildren.size() - 1; // adjustment for join strings

  int originalSize = InlineCostEstimator.getCost(n);
  if (arrayFoldedChildren.size() == 1 && foldedSize <= originalSize) {
    Node replacement = arrayFoldedChildren.get(0);
    if (replacement.getType() != Token.STRING) {
      replacement = new Node(Token.ADD, Node.newString("").copyInformationFrom(n), replacement);
    }
    n.getParent().replaceChild(n, replacement);
    reportCodeChange();
    return replacement;
  } else if (foldedSize <= originalSize) {
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    reportCodeChange();
  }
  return n;
}
