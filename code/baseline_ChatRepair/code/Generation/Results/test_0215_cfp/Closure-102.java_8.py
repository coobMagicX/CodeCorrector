public void process(Node externs, Node root) {
  // Traverse the AST with the custom callback
  NodeTraversal.traverse(compiler, root, this);

  // Conditionally make local variable names unique
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }

  // Perform removal of duplicate declarations
  removeDuplicateDeclarations(root);

  // Assuming PropogateConstantAnnotations requires a fully prepared AST,
  // make sure all previous steps have been fully applied.
  compiler.performChange(); // Hypothetical method to lock in changes if such exists

  // Propagate constant annotations across the AST
  new PropogateConstantAnnotations(compiler, assertOnChange).process(externs, root);
}
