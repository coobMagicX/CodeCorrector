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
  StringBuilder sb = null;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb == null) {
        sb = new StringBuilder();
        if (prev != null) {
          sb.append(joinString);
        }
      } else {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb != null) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = null;
      }
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb != null) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node singleNode = arrayFoldedChildren.get(0);
    if (singleNode.getType() == Token.STRING) {
      n.getParent().replaceChild(n, singleNode);
      reportCodeChange();
      return singleNode;
    }
  }

  // Replace old arrayNode children with the new folded children
  arrayNode.detachChildren();
  for (Node node : arrayFoldedChildren) {
    arrayNode.addChildToBack(node);
  }
  reportCodeChange();

  return n;
}
