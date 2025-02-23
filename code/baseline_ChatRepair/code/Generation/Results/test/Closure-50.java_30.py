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
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) || functionName == null || !functionName.getString().equals("join")) {
    return n;  // Early return if conditions are not met
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  boolean isFirst = true;

  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (!isFirst) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      isFirst = false;
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(elem));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
      isFirst = true;  // Reset for the next possible set of string literals
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(arrayNode));
  }

  if (arrayFoldedChildren.size() == 1 && NodeUtil.isString(arrayFoldedChildren.get(0))) {
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

private void reportCodeChange() {
  // Code to mark that a change was made, typically for the compiler or refactoring engine
}
