private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  Node functionName = arrayNode.getNext();
  if (functionName == null || !"join".equals(functionName.getString())) {
    return n;
  }

  Node joinArg = callTarget.getNext();  // Get the argument to `.join()`, if any.
  String joinString = ",";
  if (joinArg != null && NodeUtil.isImmutableValue(joinArg)) {
    joinString = NodeUtil.getStringValue(joinArg);
  }

  boolean allLiterals = true;
  StringBuilder sb = new StringBuilder();
  for (Node child = arrayNode.getFirstChild(); child != null; child = child.getNext()) {
    if (NodeUtil.isImmutableValue(child)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(child));
    } else {
      allLiterals = false;
      break;
    }
  }

  if (allLiterals) {
    Node replacement = Node.newString(sb.toString()).srcref(n);
    n.getParent().replaceChild(n, replacement);
    reportCodeChange();
    return replacement;
  }

  return n;
}
