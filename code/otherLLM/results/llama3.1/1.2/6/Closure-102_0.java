public void process(Node externs, Node root) {
  NodeExternsAndJS(node_externs_and_js = new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root));
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
}