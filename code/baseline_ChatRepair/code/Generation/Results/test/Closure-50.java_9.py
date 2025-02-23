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
      // Process complex expressions using a recursive mechanism or direct handling
      // for cases where simple concatenation is not valid.
      // Placeholder for potential implementation, e.g., handling complex expressions...
      return n; // Early return indicating complex cases not folded
    }
  }
  
  Node newStringNode = Node.newString(sb.toString()).srcref(n);
  n.getParent().replaceChild(n, newStringNode);
  reportCodeChange();
  return newStringNode;
}
