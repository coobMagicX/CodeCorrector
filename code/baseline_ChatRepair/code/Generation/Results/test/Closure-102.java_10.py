public void process(Node externs, Node root) {
  NodeTraversal.traverse(compiler, root, this);
  removeDuplicateDeclarations(root);

  // Ensuring each transformation has a fresh and independent view of scopes.
  compiler.reportCodeChange(); // Ensure compiler state is consistent.

  // First, handle constant propagation independently.
  if (PROPAGATE_CONSTANT_ANNOTATIONS) {
    PropogateConstantAnnotations constantPropagation = new PropogateConstantAnnotations(compiler, assertOnChange);
    NodeTraversal.traverse(compiler, externs, constantPropagation);
    NodeTraversal.traverse(compiler, root, constantPropagation);
  } 

  // Second, handle making names unique independently.
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }

  compiler.reportCodeChange