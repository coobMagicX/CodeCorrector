public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    // Note the corrected function call with proper parameter name
    t.traverseRoots(externs, parent); // Fixed: using 'parent' instead of 'root'
  }
  removeDuplicateDeclarations(root);
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}