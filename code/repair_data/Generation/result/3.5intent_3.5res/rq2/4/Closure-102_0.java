public void process(Node externs, Node root) {
  removeDuplicateDeclarations(root);

  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }

  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);

  // Additional processing logic goes here
  // ...
}