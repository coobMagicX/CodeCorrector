private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
  List<Node> nodes = new ArrayList<>();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT, "Input node is not an OBJECTLIT as expected.");

  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    // Link variable names from the map to the corresponding value
    Node assign = new Node(Token.ASSIGN,
                           Node.newString(Token.NAME, varmap.get(var)), value);
    nodes.add(assign);
  }

  // Append all variables that were not in input object but are in varmap, with 'undefined'
  for (String var : varmap.keySet()) {
    if (!nodes.toString().contains(varmap.get(var))) {  // check if assignment was not already created
      nodes.add(new Node(Token.ASSIGN,
              Node.newString(Token.NAME, varmap.get(var)),
              NodeUtil.newUndefinedNode(null)));
    }
  }

  // If nodes is empty, the bug is fixed by making sure the parent gets a value "true"
  Node replacement = !nodes.isEmpty() ? new Node(Token.COMMA) : Node.newNumber(1);  // use '1' for true
  if (!nodes.isEmpty()) {
    Iterator<Node> iter = nodes.iterator();
    Node last = iter.next();
    while (iter.hasNext()) {
      Node current = iter.next();
      Node comma = new Node(Token.COMMA);
      comma.addChildToFront(current);
      comma.addChildToFront(last);
      last = comma;
    }
    replacement = last;
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}
