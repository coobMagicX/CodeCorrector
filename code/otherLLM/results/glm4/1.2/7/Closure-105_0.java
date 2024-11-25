void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right,
                       Node parent) {
  if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
    return;
  }

  Node arrayNode = left.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      (functionName == null || !functionName.getString().equals("join"))) {
    return;
  }

  String joinString = NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder();
  int foldedSize = 0;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
    } else {
      if (sb.length() > 0) {
        foldedSize += sb.length() + 2; // + 2 for the quotes.
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    foldedSize += sb.length() + 2; // + 2 for the quotes.
    arrayFoldedChildren.add(Node.newString(sb.toString()));
  }
  foldedSize += arrayFoldedChildren.size() - 1;

  int originalSize = InlineCostEstimator.getCost(n);
  switch (arrayFoldedChildren.size()) {
    case 0:
      Node emptyStringNode = Node.newString("");
      parent.replaceChild(n, emptyStringNode);
      break;

    case 1:
      Node foldedStringNode = arrayFoldedChildren.remove(0);
      if (foldedSize > originalSize) {
        return;
      }
      arrayNode.detachChildren();
      if (foldedStringNode.getType() != Token.STRING) {
        Node replacement = new Node(Token.ADD,
            Node.newString(""), foldedStringNode);
        foldedStringNode = replacement;
      }
      parent.replaceChild(n, foldedStringNode);
      break;

    default:
      int kJoinOverhead = "[].join()".length();
      foldedSize += kJoinOverhead;
      foldedSize += InlineCostEstimator.getCost(right);
      if (foldedSize > originalSize) {
        return;
      }
      arrayNode.detachChildren();
      for (Node node : arrayFoldedChildren) {
        arrayNode.addChildToBack(node);
      }
      break;
  }
  t.getCompiler().reportCodeChange();
}