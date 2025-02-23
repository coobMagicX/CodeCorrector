void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right, Node parent) {
  if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
    return;
  }

  Node arrayNode = left.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (functionName == null || arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return;
  }

  String joinString = NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder();

  Node elem = arrayNode.getFirstChild();
  // Handle special case where first element might be an empty string.
  boolean isFirstElement = true;

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0 || !isFirstElement) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
      isFirstElement = false;
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
      isFirstElement = false;
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    parent.replaceChild(n, foldedStringNode);
  } else if (arrayFoldedChildren.size() > 1) {
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    // Recreate the join call with the modified array
    Node newJoinCall = new Node(Token.CALL, Node.newString("join"), arrayNode);
    parent.replaceChild(n, newJoinCall);
  }

  t.getCompiler().reportCodeChange();
}
