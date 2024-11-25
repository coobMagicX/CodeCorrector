public void process(Node externs, Node root) {
  // Traverse the tree to collect all variable names and constant annotations
  PropogateConstantAnnotations pcA = new PropogateConstantAnnotations(compiler, assertOnChange);
  pcA.process(externs, root);

  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    // Check for variable name conflicts after deduplication
    shouldTraverse(t, root, null); // added this line
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
}