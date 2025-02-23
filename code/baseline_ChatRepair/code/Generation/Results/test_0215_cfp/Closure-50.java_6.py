private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();
  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  
  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (functionName == null || arrayNode.getType() != Token.ARRAYLIT ||
      !functionName.getString().equals("join")) {
    return n;
  }

  // Derive the join string
  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);

  Node newJoinExpr = null;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    Node nextElem = elem.getNext();
    if (newJoinExpr == null) {
      newJoinExpr = elem.cloneTree();
    } else {
      newJoinExpr = new Node(
          Token.ADD,
          new Node(
              Token.ADD,
              newJoinExpr,
              Node.newString(joinString).copyInformationFrom(prev)),
          elem.cloneTree()
      );
    }
    prev = elem;
    elem = nextElem;
  }

  if (newJoinExpr == null) {  // If there were no elements, return empty string
    newJoinExpr = Node.newString("");
  }

  n.getParent().replaceChild(n, newJoinExpr);
  reportCodeChange();
  return newJoinExpr;
}
