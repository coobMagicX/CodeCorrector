void tryFoldStringJoin(NodeTraversal t, Node n, Node left, Node right,
                       Node parent) {
  if (!NodeUtil.isGetProp(left) || !NodeUtil.isImmutableValue(right)) {
    return;
  }

  Node arrayNode = left.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      !functionName.getString().equals("join")) {
    return;
  }

  String joinString = NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = new StringBuilder();
  int foldedSize = 0;
  Node elem = arrayNode.getFirstChild();
  // Merges adjacent String nodes.
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb.length() > 0) {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getStringValue(elem));
    } else {
      // If the node is a variable, add it to the list of nodes to be added later
      // without adding its value to the StringBuilder.
      if (NodeUtil.isVariableOrPropertyAccess(elem)) {
        arrayFoldedChildren.add(elem);
      } else {
        if (sb.length() > 0) {
          sb.append(joinString);
          foldedSize += joinString.length();
        }
        foldedSize += NodeUtil.getStringValue(elem).length();
        sb.append(NodeUtil.getStringValue(elem));
      }
    }
    elem = elem.getNext();
  }

  // Add the last StringBuilder content to the list if not empty
  if (sb.length() > 0) {
    arrayFoldedChildren.add(Node.newString(sb.toString()));
    foldedSize += sb.length();
  }

  int originalSize = InlineCostEstimator.getCost(n);
  switch (arrayFoldedChildren.size()) {
    case 0:
      Node emptyStringNode = Node.newString("");
      parent.replaceChild(n, emptyStringNode);
      break;

    case 1:
      Node foldedStringNode = arrayFoldedChildren.remove(0);
      if (foldedSize > originalSize) {
        return;
      }
      arrayNode.detachChildren();
      if (foldedStringNode.getType() != Token.STRING) {
        // If the Node is not a string literal, ensure that it is coerced to a string.
        Node replacement = new Node(Token.ADD,
            Node.newString(""), foldedStringNode);
        foldedStringNode = replacement;
      }
      parent.replaceChild(n, foldedStringNode);
      break;

    default:
      // No folding could actually be performed if the list of nodes is not empty
      // because there are variable references that cannot be converted to a single string.
      if (arrayFoldedChildren.size() != arrayNode.getChildCount()) {
        arrayNode.detachChildren();
        for (Node node : arrayFoldedChildren) {
          arrayNode.addChildToBack(node);
        }
        break;
      }
      // If all elements are variables, we cannot perform the join, so just replace the node
      return;

    t.getCompiler().reportCodeChange();
  }
}