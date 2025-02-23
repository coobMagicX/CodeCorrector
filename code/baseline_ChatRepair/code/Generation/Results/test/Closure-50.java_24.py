private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  Node arrayNode = callTarget.getFirstChild();
  if (arrayNode == null || arrayNode.getNext() == null) {
    return n;
  }
  Node functionName = arrayNode.getNext();

  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new ArrayList<>();
  StringBuilder sb = null;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb == null) {
        sb = new StringBuilder();
      } else if (prev != null) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb != null) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = null;
      }
      Node coercedNode = coerceToString(elem);
      arrayFoldedChildren.add(coercedNode);
      prev = coercedNode;
    }
    elem = elem.getNext();
  }

  if (sb != null) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, foldedStringNode);
    reportCodeChange();
    return foldedStringNode;
  } else if (!arrayFoldedChildren.isEmpty()) {
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    reportCodeChange();
  }

  return n;
}

private Node coerceToString(Node node) {
  if (node.getType() == Token.STRING) {
    return node;
  } else {
    return new Node(Token.ADD, Node.newString("").copyInformationFrom(node), node);
  }
}
