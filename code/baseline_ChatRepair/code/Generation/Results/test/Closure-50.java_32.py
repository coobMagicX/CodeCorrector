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

  Node functionName = callTarget.getNext();
  if (functionName == null || !functionName.getString().equals("join")) {
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
        Node stringNode = Node.newString(sb.toString()).copyInformationFrom(elem);
        arrayFoldedChildren.add(stringNode);
        sb.setLength(0);  // Clear the StringBuilder
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(arrayNode));
  }

  // Simplify array layout if possible
  if (arrayFoldedChildren.size() == 1) {
    Node foldedNode = arrayFoldedChildren.get(0);
    if (!NodeUtil.isString(foldedNode) && !foldedNode.isOnlyModifiesThisCall()) {
      foldedNode = IR.add(IR.string(""), foldedNode).srcref(n);
    }
    n.getParent().replaceChild(n, foldedNode);
    reportCodeChange();
    return foldedNode;
  } else if (!arrayFoldedChildren.isEmpty()) {
    // Reconstruct the array literal if it's been divided
    arrayNode.detachChildren();
    for (Node foldedChild : arrayFoldedChildren) {
      arrayNode.addChildToBack(foldedChild);
    }
    reportCodeChange();
  }

  return n;
}
