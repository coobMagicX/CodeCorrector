public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this); // Using 'this' as the handler if needed

  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root); // Corrected from 't.traverse(root)' to 't.traverseRoots(externs, root)'
  }
  
  removeDuplicateDeclarations(root); // No change, already correct
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root); // Assuming this is a static method or it takes compiler as an argument, no modification needed
}

private void removeDuplicateDeclarations(Node root) {
  Callback tickler = new ScopeTicklingCallback();
  ScopeCreator scopeCreator = new SyntacticScopeCreator(
    compiler, new DuplicateDeclarationHandler());
  NodeTraversal t = new NodeTraversal(compiler, tickler, scopeCreator);
  t.traverse(root); // Using the correct method for traversing the tree
}