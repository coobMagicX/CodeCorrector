private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) || 
      !functionName.getString().equals("join")) {
    return n;
  }
 
  Node right = functionName.getNext();
  
  // Use default separator "," if right is null or not an immutable primitive value
  String joinString = ",";
  if (right != null && NodeUtil.isImmutableValue(right)) {
    joinString = NodeUtil.getStringValue(right);
  } 
  
  StringBuilder sb = new StringBuilder();
  boolean isFirst = true;
  for (Node child = arrayNode.getFirstChild(); child != null; child = child.getNext()) {
    if (!isFirst) {
      sb.append(joinString);
    }
    isFirst = false;
    if (NodeUtil.isImmutableValue(child)) {
      sb.append(NodeUtil.getStringValue(child));
    } else {
      // We revert to the original node if there is a complex or non-immutable child, 
      // because we only consider simple and literal elements for join simplification.
      return n;
    }
  }
  
  Node newStringNode = Node.newString(sb.toString());
  newStringNode.copyInformationFrom(n);
  n.getParent().replaceChild(n, newStringNode);
  reportCodeChange();
  return newStringNode;
}
