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

  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  StringBuilder sb = new StringBuilder();
  boolean first = true;

  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (!NodeUtil.isImmutableValue(elem)) {
      return n;  // Capable of folding only when all elements are immutable.
    }
    if (first) {
      first = false;
    } else {
      sb.append(joinString);
    }
    sb.append(NodeUtil.getArrayElementStringValue(elem));
    prev = elem;
    elem = elem.getNext();
  }

  Node resultStringNode = Node.newString(sb.toString()).copyInformationFrom(prev);
  n.getParent().replaceChild(n, resultStringNode);
  reportCodeChange();
  return resultStringNode;
}
