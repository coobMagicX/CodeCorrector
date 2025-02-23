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
  if (arrayNode == null || !arrayNode.isCall()) {
    return n;
  }

  Node functionName = arrayNode.getNext();
  if (functionName == null || !functionName.getString().equals("join")) {
    return n;
  }

  if (!arrayNode.isName() || !arrayNode.getString().equals("Array")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  StringBuilder sb = new StringBuilder();
  boolean first = true;

  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (elem.getType() == Token.EMPTY) {
      elem = elem.getNext();
      continue;
    }

    if (!NodeUtil.isImmutableValue(elem)) {
      return n;  // Can only fold when all elements are immutable
    }
    if (!first) {
      sb.append(joinString);
    } else {
      first = false;
    }
    sb.append(NodeUtil.getArrayElementStringValue(elem));
    elem = elem.getNext();
  }

  Node replacementNode;
  if (sb.length() == 0) {
    replacementNode = IR.string("");
  } else {
    replacementNode = IR.string(sb.toString());
  }

  replacementNode.useSourceInfoIfMissingFromForTree(n);
  n.getParent().replaceChild(n, replacementNode);
  reportCodeChange();
  return replacementNode;
}
