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

  // TODO(user): Better source information.
  for (String var : all) {
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)),
               NodeUtil.newUndefinedNode(null)));
  }

  // All assignments evaluate to true, so make sure that the
  // expr statement evaluates to true in case it matters.
  nodes.add(new Node(Token.TRUE));

  // Join these using COMMA. A COMMA node must have 2 children, so we create a tree.
  // In the tree the first child be the COMMA to match the parser, otherwise tree equality tests fail.
  Node replacement = new Node(Token.COMMA);
  for (int i = nodes.size() - 1; i >= 0; i--) {
    if (i == nodes.size() - 1) { // The last node should not be followed by a COMMA
      replacement.addChild(nodes.get(i));
    } else {
      Node comma = new Node(Token.COMMA);
      comma.addChild(nodes.get(i));
      replacement.addChild(comma);
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