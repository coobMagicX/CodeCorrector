public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);

    // Fix issue with duplicate declarations by calling removeDuplicateDeclarations
    removeDuplicateDeclarations(root);
  }

  // Correct handling of 'arguments' variable by removing it from scope if necessary
  if (root.getKind() == Node.Kind.DECLARE) {
    Node declaredNode = (Node) root;
    if (declaredNode.getName().equals("arguments")) {
      scopeCreator.removeScope(declaredNode);
    }
  }

  // Correct handling of 'arguments' variable by removing it from scope if necessary
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);

}