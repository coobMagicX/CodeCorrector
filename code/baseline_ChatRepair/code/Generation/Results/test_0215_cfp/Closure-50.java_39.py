private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  Node arrayNode = callTarget.getFirstChild();
  
  if (arrayNode == null || arrayNode.getType() != Token.ARRAYLIT) {
    return n;
  }

  Node functionName = arrayNode.getNext();
  if (!functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  boolean allStrings = true;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (!NodeUtil.isString(elem)) {
      allStrings = false;
      break;
    }
    elem = elem.getNext();
  }

  if (allStrings) {
    StringBuilder sb = new StringBuilder();
    elem = arrayNode.getFirstChild();
    while (elem != null) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
      elem = elem.getNext();
    }
    
    Node resultNode = Node.newString(sb.toString());
    n.getParent().replaceChild(n, resultNode);
    reportCodeChange();
    return resultNode;
  }
  
  return n;
}
