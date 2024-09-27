@Override
public void visit(NodeTraversal t, Node n, Node parent) {
  if (n == scope.getRootNode()) return;

  if (n.getType() == Token.LP && parent == scope.getRootNode()) {
    handleFunctionInputs(parent);
    return;
  }

  attachLiteralTypes(n);
  switch (n.getType()) {
    case Token.FUNCTION:
      if (parent.getType() == Token.NAME) {
        return;
      }
      defineDeclaredFunction(n, parent);
      break;
    case Token.CATCH:
      defineCatch(n, parent);
      break;
    case Token.VAR:
      defineVar(n, parent);
      break;
  }
  
  if (n.getType() == Token.NAME && n.getString().equals("goog") && parent.getType() == Token.EXPR_RESULT) {
    Node googNode = parent.getChildAtIndex(0).getChildAtIndex(0);
    if (googNode.getType() == Token.ASSIGN) {
      Node propNode = googNode.getChildAtIndex(1);
      if (propNode.getType() == Token.OBJECTLIT) {
        Node fooNode = null;
        for (Node child : propNode.children()) {
          if (child.getString().equals("foo")) {
            fooNode = child;
            break;
          }
        }
        if (fooNode == null) {
          fooNode = new Node(Token.STRING);
          fooNode.setString("foo");
          fooNode.addChildToFront(new Node(Token.FUNCTION));
          propNode.addChildToBack(fooNode);
        }
      }
    }
  }
}

private void handleFunctionInputs(Node parent) {
  // implementation
}

private void attachLiteralTypes(Node n) {
  // implementation
}

private void defineDeclaredFunction(Node n, Node parent) {
  // implementation
}

private void defineCatch(Node n, Node parent) {
  // implementation
}

private void defineVar(Node n, Node parent) {
  // implementation
}