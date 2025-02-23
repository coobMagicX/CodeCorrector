private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  boolean isPreviousStringLiteral = false;
  Node prev = null;

  for (Node elem = arrayNode.getFirstChild(); elem != null; elem = elem.getNext()) {
    if (NodeUtil.isImmutableValue(elem) && elem.getType() != Token.EMPTY) {
      if (isPreviousStringLiteral) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      isPreviousStringLiteral = true;
    } else {
      if (sb.length() > 0) {
        Preconditions.checkNotNull(prev);
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb.setLength(0);
      }
      arrayFoldedChildren.add(elem);
      isPreviousStringLiteral = false;
    }
    prev = elem;
  }

  if (sb.length() > 0) {
    Preconditions.checkNotNull(prev);
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  Node newJoinCallNode = IR.call(
      IR.getprop(IR.arraylit(arrayFoldedChildren.toArray(new Node[0])), IR.string("join")),
      right != null ? right : IR.string(joinString));

  Node parent = n.getParent();
  parent.replaceChild(n, newJoinCallNode);

  reportCodeChange();
  return newJoinCallNode;
}
