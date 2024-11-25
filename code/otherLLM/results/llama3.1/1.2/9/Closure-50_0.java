private Node tryFoldArrayJoin(Node n) {
  Node callTarget = n.getFirstChild();

  if (callTarget == null || !NodeUtil.isGetProp(callTarget)) {
    return n;
  }

  Node right = callTarget.getNext();
  if (right != null) {
    if (!NodeUtil.isImmutableValue(right)) {
      return n;
    }
  }

  Node arrayNode = callTarget.getFirstChild();
  Node functionName = arrayNode.getNext();

  if ((arrayNode.getType() != Token.ARRAYLIT) ||
      !functionName.getString().equals("join")) {
    return n;
  }

  // "," is the default, it doesn't need to be explicit

  String joinString = (right == null) ? "," : NodeUtil.getStringValue(right);
  List<Node> arrayFoldedChildren = Lists.newLinkedList();
  StringBuilder sb = null;
  int foldedSize = 0;
  Node prev = null;
  Node elem = arrayNode.getFirstChild();
  
  // Merges adjacent String nodes.
  while (elem != null) {
    if (NodeUtil.isImmutableValue(elem)) {
      if (sb == null) {
        sb = new StringBuilder();
      } else {
        sb.append(joinString);
      }
      sb.append(NodeUtil.getArrayElementStringValue(elem));
    } else if (elem.getType() == Token.STRING) {
      if (sb != null) {
        Preconditions.checkNotNull(prev);
        // + 2 for the quotes.
        foldedSize += sb.length() + 2;
        arrayFoldedChildren.add(
            Node.newString(sb.toString()).copyInformationFrom(prev));
        sb = null;
      }
      foldedSize += InlineCostEstimator.getCost(elem);
      arrayFoldedChildren.add(elem);
    } else {
      if (sb != null) {
        Preconditions.checkNotNull(prev);
        // + 2 for the quotes.
        foldedSize += sb.length() + 2;
        arrayFoldedChildren.add(
            Node.newString(sb.toString()).copyInformationFrom(prev));
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
    // + 2 for the quotes.
    foldedSize += sb.length() + 2;
    arrayFoldedChildren.add(
        Node.newString(sb.toString()).copyInformationFrom(prev));
  }
  
  int originalSize = InlineCostEstimator.getCost(n);
  switch (arrayFoldedChildren.size()) {
    case 0:
      Node emptyStringNode = Node.newString("");
      n.getParent().replaceChild(n, emptyStringNode);
      reportCodeChange();
      return emptyStringNode;
    case 1:
      Node foldedStringNode = arrayFoldedChildren.remove(0);
      if (foldedSize > originalSize) {
        return n;
      }
      arrayNode.detachChildren();
      if (foldedStringNode.getType() != Token.STRING) {
        // If the Node is not a string literal, ensure that
        // it is coerced to a string.
        Node replacement = new Node(Token.ADD,
            Node.newString("").copyInformationFrom(n),
            foldedStringNode);
        foldedStringNode = replacement;
      }
      n.getParent().replaceChild(n, foldedStringNode);
      reportCodeChange();
      return foldedStringNode;
    default:
      // No folding could actually be performed.
      if (arrayFoldedChildren.size() == 1) {
        Node firstElem = arrayFoldedChildren.get(0);
        if (firstElem.getType() == Token.STRING) {
          int totalLength = 0;
          for (Node child : arrayFoldedChildren) {
            totalLength += InlineCostEstimator.getCost(child);
          }
          String joinedString = "";
          for (Node child : arrayFoldedChildren) {
            joinedString += NodeUtil.getArrayElementStringValue(child);
          }
          if (totalLength < originalSize) {
            Node newNode = Node.newString(joinedString);
            n.getParent().replaceChild(n, newNode);
            reportCodeChange();
            return newNode;
          }
        }
      }
      
      // Handle other types of elements
      for (Node child : arrayFoldedChildren) {
        if (child.getType() == Token.NUMBER || child.getType() == Token.STRING) {
          int totalLength = 0;
          for (Node c : arrayFoldedChildren) {
            totalLength += InlineCostEstimator.getCost(c);
          }
          String joinedString = "";
          for (Node c : arrayFoldedChildren) {
            if (c.getType() == Token.NUMBER) {
              joinedString += NodeUtil.getNumberValue(c).toString();
            } else {
              joinedString += NodeUtil.getArrayElementStringValue(c);
            }
          }
          if (totalLength < originalSize) {
            Node newNode = Node.newString(joinedString);
            n.getParent().replaceChild(n, newNode);
            reportCodeChange();
            return newNode;
          }
        }
      }

      // If no folding is possible for the current array, try to fold its elements
      List<Node> newFoldedChildren = Lists.newArrayList();
      for (Node child : arrayFoldedChildren) {
        Node foldedChild = tryFold(child);
        if (foldedChild != null) {
          newFoldedChildren.add(foldedChild);
        } else {
          newFoldedChildren.add(child);
        }
      }

      // Recursively call tryFold on the new list of children
      n.getParent().replaceChild(n, new Node(Token.ARRAYLIT, newFoldedChildren));
      return n;
  }

  return n;
}