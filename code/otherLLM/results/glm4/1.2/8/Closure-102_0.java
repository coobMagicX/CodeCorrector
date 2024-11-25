public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}

// Inside MakeDeclaredNamesUnique class
public void onRedeclaration(
    Scope s, String name, Node n, Node parent, Node gramps,
    Node nodeWithLineNumber) {
  Preconditions.checkState(n.getType() == Token.NAME);

  if (parent.getType() == Token.VAR) {
    Preconditions.checkState(parent.hasOneChild());

    // Remove the parent VAR.
    parent.removeChild(n);
    Node value = n.getFirstChild();

    // Only rename variables that are not being initialized or empty reference
    if (value != null && value.getType() != Token.COMMA) {
      Node replacement = new Node(Token.ASSIGN, n, value);

      // Replace the VAR with an EXPRESSION_RESULT node containing the assignment
      gramps.replaceChild(parent, new Node(Token.EXPR_RESULT, replacement));
    } else {
      // Handle empty reference and special cases like "for (var a in b) ..."
      if (NodeUtil.isStatementBlock(gramps)) {
        gramps.removeChild(parent);
      } else if (gramps.getType() == Token.FOR) {
        parent.removeChild(n);
        gramps.replaceChild(parent, n);
      } else {
        Preconditions.checkState(gramps.getType() == Token.LABEL);
        gramps.replaceChild(parent, new Node(Token.EMPTY));
      }
    }

    reportCodeChange("Duplicate VAR declaration");
  }
}