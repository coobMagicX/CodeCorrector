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
    if (!varmap.containsKey(var)) {
      throw new RuntimeException("Variable '" + var + "' not found in varmap");
    }
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)),
               NodeUtil.newUndefinedNode(null)));
  }

  Node replacement;
    // All assignments evaluate to true, so make sure that the
    // expr statement evaluates to true in case it matters.
    nodes.add(new Node(Token.TRUE));

    // Join these using COMMA.  A COMMA node must have 2 children, so we
    // create a tree. In the tree the first child be the COMMA to match
    // the parser, otherwise tree equality tests fail.
    List<Node> result = new ArrayList<>();
    for (Node n : nodes) {
      if (n.getType() == Token.ASSIGN && n.getChildCount() > 1) {
        Node varNode = n.getFirstChild();
        Node valueNode = n.getNext();
        while (valueNode != null) {
          result.add(new Node(Token.ASSIGN, varNode, valueNode));
          valueNode = valueNode.getNext();
        }
      } else {
        result.add(n);
      }
    }
    nodes.clear();
    nodes.addAll(result);

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(
        replace, NodeUtil.newExpr(nodes.get(0)));
  } else {
    replace.getParent().replaceChild(replace, new Node(Token.COMMA));
    for (Node n : nodes) {
      replace.getParent().replaceChild(new Node(Token.COMMA), n);
    }
  }
}