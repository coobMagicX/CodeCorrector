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
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)), value));
    all.remove(var);
  }

  // Add undefined assignments for remaining variables
  for (String var : all) {
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)),
               NodeUtil.newUndefinedNode(null)));
  }

  Node replacement;
  // All assignments evaluate to true, so add a TRUE node to ensure the expression is truthy
  nodes.add(new Node(Token.TRUE));

  // Join these using COMMA. A COMMA node must have at least two children.
  nodes = Lists.reverse(nodes);
  replacement = new Node(Token.COMMA);
  Node cur = replacement;
  for (Node node : nodes) {
    cur.addChildToFront(node);
    if (!nodes.get(nodes.size() - 1).equals(node)) { // Avoid adding a COMMA after the last node
      cur.addChildToFront(new Node(Token.COMMA));
      cur = cur.getFirstChild();
    }
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