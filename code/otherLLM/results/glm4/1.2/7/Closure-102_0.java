public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this); // Using the existing method

  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }

  removeDuplicateDeclarations(root); // Method used correctly

  // Correctly using the PropogateConstantAnnotations method with compiler and assertOnChange
  PropogateConstantAnnotations propagator = new PropogateConstantAnnotations(compiler, assertOnChange);
  propagator.process(externs, root); // Direct call to process method of PropogateConstantAnnotations
}