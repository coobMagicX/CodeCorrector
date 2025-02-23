private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
  // Compute all of the assignments necessary
  List<Node> nodes = new LinkedList<>();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val != null && val.isObjectLit(), "Expected OBJECTLIT but was " + val);

  Set<String> all = new LinkedHashSet<>(varmap.keySet());
  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String varName = key.getString();
    Node value = key.getFirstChild().detach(); // Fixed: Use detach instead of removeFirstChild() which throws if null.
    if (varmap.containsKey(varName)) {
      Node assignTarget = Node.newString(Token.NAME, varmap.get(varName));
      nodes.add(new Node(Token.ASSIGN, assignTarget, value).useSourceInfoIfMissingFromForTree(key));
    }
    all.remove(varName);
  }

  // Handle undefined variables
  for (String var : all) {
    Node target = Node.newString(Token.NAME, varmap.get(var));
    Node undefinedVal = NodeUtil.newUndefinedNode(null);
    nodes.add(new Node(Token.ASSIGN, target, undefinedVal).useSourceInfoFrom(target));
  }

  Node replacement = new Node(Token.COMMA);

  // Chain assignments with COMMA
  Node lastNode = null;
  for (Node node : nodes) {
    if (lastNode != null) {
      node = new Node(Token.COMMA, lastNode, node);
    }
    lastNode = node;
  }
  if (lastNode != null) {
    replacement = lastNode;
  }

  Node replace = ref.getParent();
  if (replace != null && replacement != null) {
    replacement.copyInformationFromForTree(replace);
    if (replace.getType() == Token.VAR) {
      replace.getParent().replaceChild(replace, new Node(Token.EXPR_RESULT, replacement));
    } else {
      replace.getParent().replaceChild(replace, replacement);
    }
  }
}
