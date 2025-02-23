public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  
  // Move constant propagation before the renaming.
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
  
  // Apply unique name changes after constants are propagated.
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  
  removeDuplicateDeclarations(root);
}
