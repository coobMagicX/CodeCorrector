private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = Lists.newArrayList();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);

  Set<String> allVars = new HashSet<>(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();

    // Check if the variable is defined in varmap
    if (allVars.contains(var)) {
      allVars.remove(var);
      
      // Create a new assignment node for each variable assignment
      nodes.add(new Node(Token.ASSIGN,
                         Node.newString(Token.NAME, var),
                         value));
    }
  }

  // Add undefined nodes for variables that are not assigned any values
  for (String var : allVars) {
    nodes.add(new Node(Token.ASSIGN,
                       Node.newString(Token.NAME, varmap.get(var)),
                       NodeUtil.newUndefinedNode(null)));
  }

  // Ensure the expression evaluates to true by adding a TRUE node at the end
  nodes.add(new Node(Token.TRUE));

  // Join these using COMMA. A COMMA node must have 2 children, so we create a tree.
  Node replacement = new Node(Token.COMMA);
  Node cur = replacement;
  for (int i = nodes.size() - 1; i >= 0; --i) {
    cur.addChildToFront(nodes.get(i));
    if (i > 0) { // Do not add comma after the last node
      cur.addChildToFront(new Node(Token.COMMA));
    }
    cur = nodes.get(i);
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