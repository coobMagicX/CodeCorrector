private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = callTarget.getSecondChild();

  if (arrayNode == null || functionName == null || !functionName.getString().equals("join")) {
    return n;
  }

  Node joinArg = functionName.getNext();
  String joinString = joinArg != null && NodeUtil.isStringLiteral(joinArg) ? joinArg.getString() : ",";

  StringBuilder sb = new StringBuilder();
  Node elem = arrayNode.getFirstChild();
  boolean first = true;

  while (elem != null) {
    if (!NodeUtil.isImmutableValue(elem)) {
      return n; // Cannot fold if the elements contain mutable values
    }
    if (!first) {
      sb.append(joinString);
    }
    first = false;
    sb.append(NodeUtil.getArrayElementStringValue(elem));
    elem = elem.getNext();
  }

  Node replacement = Node.newString(sb.toString()).srcref(n);
  n.getParent().replaceChild(n, replacement);
  reportCodeChange();
  return replacement;
}
