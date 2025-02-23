private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  Node arrayNode = callTarget.getFirstChild();
  if (arrayNode == null || arrayNode.getNext() != callTarget.getLastChild()) {
    return n;
  }

  Node functionName = callTarget.getLastChild();
  if (functionName == null || !functionName.getString().equals("join") ||
      arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  String joinString = (right != null && NodeUtil.isImmutableValue(right)) ? NodeUtil.getStringValue(right) : ",";
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (prev != null) { // add the join string between known string elements
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) { // flush the collected strings into a single node
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0); // reset string builder
      }
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) { // add any remaining string content
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node singleResult = arrayFoldedChildren.get(0);
    if (singleResult.getType() != Token.STRING) {
      // need to ensure that the single result is correctly typed as a String
      singleResult = IR.add(IR.string(""), singleResult);
    }
    n.getParent().replaceChild(n, singleResult);
    reportCodeChange();
    return singleResult;
  } else if (arrayFoldedChildren.size() > 1) {
    arrayNode.detachChildren();
    for (Node child : arrayFoldedChildren) {
      arrayNode.addChildToBack(child);
    }
    reportCodeChange();
  }

  return n;
}
