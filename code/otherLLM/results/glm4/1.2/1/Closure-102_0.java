public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  
  // Ensure that removeDuplicateDeclarations is called with the correct callback
  removeDuplicateDeclarations(root, new ScopeTicklingCallback());
  
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}

// Modify the removeDuplicateDeclarations method to accept a Callback
private void removeDuplicateDeclarations(Node root, Callback tickler) {
  ScopeCreator scopeCreator = new SyntacticScopeCreator(
      compiler, new DuplicateDeclarationHandler());
  NodeTraversal t = new NodeTraversal(compiler, tickler, scopeCreator);
  t.traverse(root);
}