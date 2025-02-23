private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();
  
  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  
  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return n;
  }

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<Node>();
  StringBuilder sb = new StringBuilder();
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  
  // Loop over all elements in the array literal
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY) {
      if (sb.length() > 0 && elem.getType() != Token.EMPTY) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }

  // Determine whether folding reduces the size
  int originalSize = InlineCostEstimator.getCost(n);
  if (arrayFoldedChildren.size() == 1) {
    Node foldedNode = arrayFoldedChildren.get(0);
    if (InlineCostEstimator.getCost(foldedNode) >= originalSize) {
      return n;
    }
    arrayNode.detachChildren();
    n.getParent().replaceChild(n, foldedNode);
    reportCodeChange();
    return foldedNode;
  }

  if (arrayFoldedChildren.size() > 1) {
    arrayNode.detachChildren();
    for (Node child : arrayFoldedChildren) {
      arrayNode.addChildToBack(child);
    }
    reportCodeChange();
  }

  return n;
}
