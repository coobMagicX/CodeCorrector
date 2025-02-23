private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
  List<Node> nodes = new ArrayList<Node>();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT, "Input node is not an OBJECTLIT as expected.");

  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    Node assign = new Node(Token.ASSIGN,
                           Node.newString(Token.NAME, varmap.get(var)), value);
    nodes.add(assign);
  }

  // Handle the remaining variables that should be set to undefined
  for (String var : varmap.keySet()) {
    if (!containsVariable(nodes, varmap.get(var))) {
      nodes.add(new Node(Token.ASSIGN,
              Node.newString(Token.NAME, varmap.get(var)),
              NodeUtil.newUndefinedNode(null)));
    }
  }

  Node replacement;
  if (nodes.size() == 1) {
    replacement = nodes.get(0);
  } else if (nodes.size() > 1) {
    Node commaNode = new Node(Token.COMMA);
    Iterator<Node> nodeIterator = nodes.iterator();
    Node first = nodeIterator.next();
    Node second = nodeIterator.next();
    commaNode.addChildToBack(first);
    commaNode.addChildToBack(second);
    
    while (nodeIterator.hasNext()) {
      Node currentNode = nodeIterator.next();
      Node newCommaNode = new Node(Token.COMMA);
      newCommaNode.addChildToBack(commaNode);
      newCommaNode.addChildToBack(currentNode);
      commaNode = newCommaNode;
    }

    replacement = commaNode;
  } else {
    // Default case, add a Node that evaluates to true.
    replacement = new Node(Token.TRUE);
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}

private boolean containsVariable(List<Node> nodes, String var) {
  for (Node node : nodes) {
    if (node.toString().contains("\"" + var + "\"")) {
      return true;
    }
  }
  return false;
}
