private Node tryFoldArrayJoin(Node n) {
  // Current processing remains untouched until determining how the nodes are handled
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null && !NodeUtil.isImmutableValue(right)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);

  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node lastFoldableNode = null;
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (lastFoldableNode != null) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      lastFoldableNode = elem;
    } else {
      if (lastFoldableNode != null) {
        arrayFoldedChildren.add(Node.newString(sb.toString().replaceAll("^\"|\"$", ""))
                                .copyInformationFrom(lastFoldableNode));
        sb = new StringBuilder();
        lastFoldableNode = null;
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (lastFoldableNode != null) {
    arrayFoldedChildren.add(Node.newString(sb.toString().replaceAll("^\"|\"$", ""))
                            .copyInformationFrom(lastFoldableNode));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node singleResult = arrayFoldedChildren.get(0);
    if (!NodeUtil.isString(singleResult)) {
      singleResult = new Node(Token.ADD, Node.newString(""), singleResult)
                         .copyInformationFrom(singleResult);
    }
    n.getParent().replaceChild(n, singleResult);
    reportCodeChange();
    return singleResult;
  } else if (!arrayFoldedChildren.isEmpty()) {
    arrayNode.detachChildren();
    for (Node childNode : arrayFoldedChildren) {
      arrayNode.addChildToBack(childNode);
    }
    reportCodeChange();
  }

  return n;
}
