public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    // Ensuring that the variable 'renamer' is accessible to the NodeTraversal object
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
  // Creating a PropogateConstantAnnotations object with proper parameters
  new PropogateConstantAnnotations(compiler, assertOnChange).process(externs, root);

  // Ensure that changes are reported if necessary
  reportCodeChange("Changes made by process method");
}