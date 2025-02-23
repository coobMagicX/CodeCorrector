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

  if (arrayNode.getType() != Token.ARRAYLIT ||
      !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = right == null ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;

  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (prev != null && NodeUtil.isImmutableValue(prev)) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0);  // reset the StringBuilder for reuse
      }
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  if (arrayFoldedChildren.size() == arrayNode.getChildCount()) {
    return n; // No changes required
  }

  Node replacementNode = new Node(Token.ARRAYLIT);
  for (Node child : arrayFoldedChildren) {
    replacementNode.addChildToBack(child);
  }

  Node joinCall = new Node(Token.CALL, n.getFirstChild().cloneTree());
  joinCall.addChildToBack(replacementNode);
  if (right != null) {
    joinCall.addChildToBack(right.cloneTree());
  }

  n.getParent().replaceChild(n, joinCall);
  reportCodeChange();

  return joinCall;
}
