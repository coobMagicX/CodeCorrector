private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = Lists.newArrayList();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);
  Set<String> all = Sets.newLinkedHashSet(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    // TODO(user): Copy type information.
    nodes.add(
        new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), value));
    all.remove(var);
  }

  // TODO(user): Better source information.
  for (String var : all) {
    nodes.add(
        new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), NodeUtil.newUndefinedNode(null)));
  }

  Node replacement;
  // All assignments evaluate to true, so make sure that the
  // expr statement evaluates to true in case it matters.
  Node trueNode = new Node(Token.TRUE);
  if (nodes.isEmpty()) {
    replacement = trueNode;
  } else {
    nodes.add(trueNode);
    replacement = new Node(Token.COMMA);
    for (int i = nodes.size() - 1; i >= 0; i--) {
      Node child = nodes.get(i);
      child.detach();
      replacement.addChildToFront(child);
    }
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}