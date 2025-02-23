private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  Node functionName = callTarget.getSecondChild();
  if (functionName == null || !"join".equals(functionName.getString())) {
    return n;
  }

  Node joinArg = callTarget.getNext();
  String joinString = (joinArg != null && NodeUtil.isImmutableValue(joinArg)) ? NodeUtil.getStringValue(joinArg) : ",";

  boolean allStrings = true;
  StringBuilder sb = new StringBuilder();
  for (Node child = arrayNode.getFirstChild(); child != null; child = child.getNext()) {
    if (NodeUtil.isImmutableValue(child)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(child));
    } else {
      allStrings = false;
      break;
    }
  }

  if (allStrings) {
    Node replacement = Node.newString(sb.toString()).srcref(n);
    n.getParent().replaceChild(n, replacement);
    reportCodeChange();
    return replacement;
  }

  return n;
}
