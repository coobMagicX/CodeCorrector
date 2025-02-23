private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();
  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null && !NodeUtil.isImmutableValue(right)) {
    return n; // Ensure argument to joining is immutable.
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();
  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n; // Validate structure is specific array.join call.
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = new StringBuilder();
  Node elem = arrayNode.getFirstChild();

  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      sb.append(NodeUtil.getArrayElementStringValue(elem));
      if (elem.getNext() != null && elem.getNext().isString()) {
          // Append join string only between string literals to prevent improper separators.
          sb.append(joinString);
      }
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(elem));
        sb = new StringBuilder(); // Reset StringBuilder after recording the folded string.
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  // Add remaining accumulated string literals.
  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).srcrefIfMissing(arrayNode));
  }

  if (arrayFoldedChildren.size() == 1) {
    Node singleChild = arrayFoldedChildren.get(0);
    n.getParent().replaceChild(n, singleChild);
    reportCodeChange();
    return singleChild;
  } else if (!arrayFoldedChildren.isEmpty()) {
    // Replace arrayNode children with folded children.
    arrayNode.detachChildren();
    for (Node child : arrayFoldedChildren) {
      arrayNode.addChildToBack(child);
    }
    reportCodeChange();
  }

  return n;
}
