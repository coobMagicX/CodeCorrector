public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
  
  // Create an instance of PropogateConstantAnnotations
  PropogateConstantAnnotations pca = new PropogateConstantAnnotations(compiler, assertOnChange);
  // Call the process method on pca
  pca.process(externs, root);
}