private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = Lists.newArrayList();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);

  // Copy type information before adding to the node list.
  Set<String> all = Sets.newLinkedHashSet(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();

    // Ensure that we are working with a string representing the variable name
    String mappedVarName = varmap.get(var);
    if (mappedVarName == null) {
      throw new IllegalArgumentException("Variable " + var + " not found in map.");
    }
    
    nodes.add(new Node(Token.ASSIGN,
                       Node.newString(Token.NAME, mappedVarName), value));
    all.remove(var);
  }

  // Add undefined nodes for remaining variables and copy their type information
  for (String var : all) {
    String mappedVarName = varmap.get(var);
    nodes.add(new Node(Token.ASSIGN,
                       Node.newString(Token.NAME, mappedVarName),
                       NodeUtil.newUndefinedNode(null)));
  }

  // All assignments evaluate to true, so make sure that the
  // expr statement evaluates to true in case it matters.
  nodes.add(new Node(Token.TRUE));

  // Join these using COMMA. A COMMA node must have at least 2 children.
  nodes = Lists.reverse(nodes);
  Node replacement = new Node(Token.COMMA);
  Node cur = replacement;
  for (Node node : nodes) {
    cur.addChildToFront(node);
    if (cur.getChildren().size() < 2) { // Add comma only when not the last pair
      cur.addChildToFront(new Node(Token.COMMA));
    }
    cur = cur.getFirstChild(); // Move to the next child in the tree
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(
        replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}