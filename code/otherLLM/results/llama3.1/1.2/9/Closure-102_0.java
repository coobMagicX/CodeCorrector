public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    // Check if 'arguments' is a function parameter
    if (t.isParameter("arguments")) {
      // If so, handle it accordingly in the transformation logic
      t.handleArguments(externs, root);
    }
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}