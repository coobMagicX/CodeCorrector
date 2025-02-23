void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right, Node parent) {
  if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
    return;
  }

  Node arrayNode = left.getFirstChild();
  Node functionName = arrayNode.getNext();

  if (functionName == null || arrayNode.getType() != Token.ARRAYLIT || !functionName.getString().equals("join")) {
    return;
  }

  String joinString = NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder();
  boolean changesMade = false;

  Node elem = arrayNode.getFirstChild();
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem) && elem.getType() == Token.STRING) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
      changesMade = true;
    } else {
      if (sb.length() > 0) {
        arrayFoldedChildren.add(Node.newString(sb.toString()));
        sb = new StringBuilder();
      }
      arrayFoldedChildren.add(elem);
    }
    elem = elem.getNext();
  }

  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
  }

  // No meaningful change to the array, skip the rest
  if (!changesMade && arrayFoldedChildren.size() == arrayNode.getChildCount()) {
    return;
  }

  if (arrayFoldedChildren.isEmpty()) {
    parent.replaceChild(n, Node.newString(""));
  } else if (arrayFoldedChildren.size() == 1) {
    Node foldedStringNode = arrayFoldedChildren.get(0);
    parent.replaceChild(n, foldedStringNode);
  } else {
    arrayNode.detachChildren();
    for (Node node : arrayFoldedChildren) {
      arrayNode.addChildToBack(node);
    }
    parent.replaceChild(n, new Node(Token.CALL, left, right));
  }

  t.getCompiler().reportCodeChange();
}
