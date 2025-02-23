private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (!functionName.getString().equals("join") || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        Node newStringNode = Node.newString(sb.toString()).copyInformationFrom(elem);
        arrayFoldedChildren.add(newStringNode);
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    Node newStringNode = Node.newString(sb.toString()).copyInformationFrom(arrayNode);
    arrayFoldedChildren.add(newStringNode);
  }

  if (arrayFoldedChildren.size() == 1) {
    Node replacementNode = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, replacementNode);
    reportCodeChange();
    return replacementNode;
  } else {
    // Replace the content of the original array node if there are multiple children
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    reportCodeChange();
    return n;
  }
}
