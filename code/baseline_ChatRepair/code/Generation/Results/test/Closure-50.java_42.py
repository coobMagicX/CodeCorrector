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
  if (functionName == null || !functionName.getString().equals("join")) {
    return n;
  }
  
  // Determine the join string
  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  
  // Handle the special case of an empty array
  if (!arrayNode.hasChildren()) {
    Node emptyStringNode = Node.newString("");
    n.getParent().replaceChild(n, emptyStringNode);
    reportCodeChange();
    return emptyStringNode;
  }
  
  StringBuilder sb = new StringBuilder();
  Node elem = arrayNode.getFirstChild();
  boolean first = true;
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (!first) {
        sb.append(joinString);
      } else {
        first = false;
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      // Can't fold non-constant or non-string elements fully
      // If there's anything accumulated, create a string node for that part
      if (sb.length() > 0) {
        createStringNode(sb, n, arrayNode);
        sb = new StringBuilder(); // reset builder
        first = true; // reset the first element flag
      }
      arrayNode.addChildToBack(elem);
    }
    elem = elem.getNext();
  }
  
  // If there's anything left in the string builder, add it as a node
  if (sb.length() > 0) {
    createStringNode(sb, n, arrayNode);
  }
  
  // No elements means it should be an empty string
  if (!arrayNode.hasChildren()) {
    Node emptyStringNode = Node.newString("");
    n.getParent().replaceChild(n, emptyStringNode);
    reportCodeChange();
    return emptyStringNode;
  }
  
  return n;
}

private void createStringNode(StringBuilder sb, Node originalNode, Node parent) {
  Node newStringNode = Node.newString(sb.toString()).copyInformationFrom(originalNode);
  parent.addChildToBack(newStringNode);
}
