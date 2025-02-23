private void replaceAssignmentExpression(Var v, Reference ref,
                                         Map<String, String> varmap) {
  List<Node> nodes = new ArrayList<Node>();  // Specify type explicitly
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);
  Set<String> all = new LinkedHashSet<String>(varmap.keySet());  // Specify type explicitly
  
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), value));
    all.remove(var);
  }

  for (String var : all) {
    nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)),
                        NodeUtil.newUndefinedNode(null)));
  }

  // Ensure the last evaluated expression is 'true'.
  nodes.add(new Node(Token.TRUE));

  // Construct COMMA-separated node chain
  Node replacement = nodes.remove(nodes.size() - 1);
  for (int i = nodes.size() - 1; i >= 0; i--) {
    replacement = new Node(Token.COMMA, nodes.get(i), replacement);
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}
