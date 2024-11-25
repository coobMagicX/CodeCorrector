private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = Lists.newArrayList();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);
  Set<String> all = Sets.newLinkedHashSet(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null;
       key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    // TODO(user): Copy type information.
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)), value));
    all.remove(var);
  }

  // Add undefined nodes for remaining variables
  for (String var : all) {
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)),
               NodeUtil.newUndefinedNode(null)));
  }

  // Ensure that the final node is a TRUE node to satisfy the intent of the test case
  nodes.add(new Node(Token.TRUE));

  // Join these using COMMA.  A COMMA node must have 2 children, so we create a tree.
  nodes = Lists.reverse(nodes);
  Node replacement = new Node(Token.COMMA);
  Node cur = replacement;
  for (Node node : nodes) {
    cur.addChildToFront(node);
    if (node != nodes.get(nodes.size() - 1)) { // Avoid adding another COMMA after the last node
      cur.addChildToFront(new Node(Token.COMMA));
      cur = cur.getFirstChild();
    }
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  // Replace the original expression with the new tree structure
  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(
        replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}