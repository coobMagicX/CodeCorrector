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
  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  // Define the join string, default is ","
  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = null;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  boolean started = false;

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb == null) {
        sb = new StringBuilder();
      } else {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb != null) {
        Preconditions.checkNotNull(prev);
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = new StringBuilder();
      }
      if (started) {
        arrayFoldedChildren.add(Node.newString(joinString).copyInformationFrom(elem));
      }
      arrayFoldedChildren.add(elem);
      started = true;
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb != null) {
    Preconditions.checkNotNull(prev);
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  arrayNode.detachChildren();
  for (Node node : arrayFoldedChildren) {
    arrayNode.addChildToBack(node);
  }
  reportCodeChange();
  return n;
}
