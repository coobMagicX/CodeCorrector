private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (!NodeUtil.isImmutableValue(right) && right != null) {
    return n;
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (arrayNode.getType() != Token.ARRAYLIT ||
      !"join".equals(functionName.getString())) {
    return n;
  }

  // "," is the default, it doesn't need to be explicit
  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = new LinkedList<>();
  StringBuilder sb = null;
  int foldedSize = 0;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();

  // Merges adjacent String nodes or empty elements like null/undefined.
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) || elem.getType() == Token.EMPTY ||
        elem.getType() == Token.NULL || elem.getString().equals("undefined")) {
      if (sb == null) {
        sb = new StringBuilder();
      } else if (sb.length() > 0) { // Avoid adding unnecessary join strings.
        sb.append(joinString);
      }
      String elemValue = elem.getType() == Token.NULL || elem.getString().equals("undefined") 
                         ? "" : NodeUtil.getArrayElementStringValue(elem);
      sb.append(elemValue);
    } else {
      if (sb != null) {
        Preconditions.checkNotNull(prev);
        foldedSize += sb.length() + 2; // +2 for the quotes.
        arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = null;
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    }
    prev = elem;
    elem = elem.getNext();
  }

  if (sb != null) {
    Preconditions.checkNotNull(prev);
    foldedSize += sb.length() + 2; // +2 for the quotes.
    arrayFoldedChildren.add(Node.newString(sb.toString()).copyInformationFrom(prev));
  }
  // one for each comma.
  foldedSize += arrayFoldedChildren.size() - 1;

  int originalSize = InlineCostEstimator.getCost(n);
  if (foldedSize > originalSize) {
    return n;
  }

  Node newNode = NodeUtil.buildStringNodeFromArray(arrayFoldedChildren, joinString);
  n.getParent().replaceChild(n, newNode);
  reportCodeChange();
  return newNode;
}
