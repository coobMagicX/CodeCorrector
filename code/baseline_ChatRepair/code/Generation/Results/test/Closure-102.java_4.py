public void process(Node externs, Node root) {
  // Traverse the AST using this class as a callback.
  NodeTraversal.traverse(compiler, root, this);

  // Optionally make local names unique to avoid name collision issues
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }

  // Propagate constant annotations, which might introduce changes necessitating re-traversal
  new PropagateConstantAnnotations(compiler, assertOnChange).process(externs, root);

  // Since removing duplicates and constant propagation could interfere with each other,
  // Ensure that duplicate declarations are removed after all transformations to the graph.
  removeDuplicateDeclarations(root);

  // Consider traversing again if necessary (Depending on semantics of transformations)
  NodeTraversal.traverse(compiler, root, this);
}
