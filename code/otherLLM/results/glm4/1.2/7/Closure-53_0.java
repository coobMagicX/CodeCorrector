private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = Lists.newArrayList();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);
  Set<String> allVars = Sets.newLinkedHashSet(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String varName = key.getString();
    Node value = key.removeFirstChild();
    nodes.add(new Node(Token.ASSIGN,
                       Node.newString(Token.NAME, varmap.get(varName)), value));
    allVars.remove(varName);
  }

  // Add undefined assignments for remaining variables
  for (String varName : allVars) {
    Node undefinedNode = NodeUtil.newUndefinedNode(null);
    nodes.add(new Node(Token.ASSIGN,
                       Node.newString(Token.NAME, varmap.get(varName)), undefinedNode));
  }

  // Ensure the last expression is always true
  nodes.add(new Node(Token.TRUE));

  // Create a comma-separated list of assignments
  Node replacement = new Node(Token.COMMA);
  for (int i = 0; i < nodes.size(); ++i) {
    replacement.addChildToFront(nodes.get(i));
    if (i < nodes.size() - 1) { // Add a COMMA node only before the last element
      replacement.addChildToFront(new Node(Token.COMMA));
    }
  }

  // Replace the original expression with the new one
  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);
  replace.getParent().replaceChild(replace, replacement);
}