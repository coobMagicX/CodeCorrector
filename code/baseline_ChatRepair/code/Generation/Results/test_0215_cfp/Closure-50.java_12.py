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
  List<Node> arrayFoldedChildren = new LinkedList<Node>();
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
        sb.setLength(0); // Clear the StringBuilder for possible later immutable values
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

  if (arrayFoldedChildren.size() == 1 && right == null) {
      // If the array folded into a single string and no custom separator is defined, simply return it
      return arrayFoldedChildren.get(0);
  }

  // Constructing new join call with possibly fewer arguments
  Node newArrayNode = IR.arraylit(arrayFoldedChildren.toArray(new Node[arrayFoldedChildren.size()]));
  Node newJoinCall = IR.call(
      IR.getprop(newArrayNode, IR.string("join")),
      right != null ? right : IR.string(joinString)
  );

  // Replace original join call node with new optimized join call node
  n.getParent().replaceChild(n, newJoinCall);
  reportCodeChange();
  return newJoinCall;
}
