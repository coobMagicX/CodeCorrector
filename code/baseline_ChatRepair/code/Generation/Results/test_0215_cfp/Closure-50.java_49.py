private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null && !NodeUtil.isImmutableValue(right)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  Node functionName = arrayNode.getNext();
  if (!"join".equals(NodeUtil.getStringValue(functionName))) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (prev != null) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      prev = elem;
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0);
        prev = null;
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() > 1 || (arrayFoldedChildren.size() == 1 && !NodeUtil.isString(arrayFoldedChildren.get(0)))) {
    Node newNode = new Node(Token.ADD);
    newNode.addChildToBack(Node.newString("").copyInformationFrom(n));
    for (Node childNode : arrayFoldedChildren) {
      newNode.addChildToBack(childNode);
    }
    n.getParent().replaceChild(n, newNode);
    reportCodeChange();
    return newNode;
  } else if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, foldedStringNode);
    reportCodeChange();
    return foldedStringNode;
  } else {
    Node emptyStringNode = Node.newString("");
    n.getParent().replaceChild(n, emptyStringNode);
    reportCodeChange();
    return emptyStringNode;
  }
}
