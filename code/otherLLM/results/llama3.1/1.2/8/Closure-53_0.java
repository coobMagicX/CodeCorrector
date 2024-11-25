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
    // Correct Type Inference and Tree Equality Test Fixing
    if (value.getType() == Token.STRING) {
      value.setType(Token.NAME);
    }
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)), value));
    all.remove(var);
  }

  // Variable Initialization and Assignment Order Correction
  for (String var : all) {
    if (!varmap.containsKey(var)) { // Initialize variables without initializers
      nodes.add(
        new Node(Token.ASSIGN,
                 Node.newString(Token.NAME, var), NodeUtil.newUndefinedNode(null)));
    }
    Node value = NodeUtil.newUndefinedNode(null);
    if (varmap.containsKey(var)) {
      String valueStr = varmap.get(var);
      Node valueNode = new Node(Token.STRING, valueStr);
      // Correct Type Inference
      if (valueStr.contains("true") || valueStr.contains("false")) {
        valueNode.setType(Token.NAME);
      }
      nodes.add(
        new Node(Token.ASSIGN,
                 Node.newString(Token.NAME, varmap.get(var)), valueNode));
    }
  }

  Node replacement;
    // All assignments evaluate to true, so make sure that the
    // expr statement evaluates to true in case it matters.
    nodes.add(new Node(Token.TRUE));

    // Join these using COMMA.  A COMMA node must have 2 children, so we
    // create a tree. In the tree the first child be the COMMA to match
    // the parser, otherwise tree equality tests fail.
    nodes = Lists.reverse(nodes);
    replacement = new Node(Token.COMMA);
    Node cur = replacement;
    int i;
    for (i = 0; i < nodes.size() - 2; i++) {
      cur.addChildToFront(nodes.get(i));
      Node t = new Node(Token.COMMA);
      cur.addChildToFront(t);
      cur = t;
    }
    cur.addChildToFront(nodes.get(i));
    cur.addChildToFront(nodes.get(i + 1));

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(
        replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}