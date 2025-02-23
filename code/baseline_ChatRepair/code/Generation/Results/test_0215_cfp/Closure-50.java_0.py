private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();
  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();
  if (functionName == null || !functionName.getString().equals("join") || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  Node separatorArg = functionName.getNext();
  String joinString = (separatorArg != null && NodeUtil.isImmutableValue(separatorArg)) ? NodeUtil.getStringValue(separatorArg) : ",";

  StringBuilder sb = new StringBuilder();
  boolean first = true;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (!first) {
      sb.append(joinString);
    }
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      // If we encounter a non-immutable value, we can't fold past this point.
      return n;
    }
    first = false;
    elem = elem.getNext();
  }

  Node replacementNode = Node.newString(sb.toString()).useSourceInfoFrom(n);
  n.getParent().replaceChild(n, replacementNode);
  reportCodeChange();
  return replacementNode;
}
