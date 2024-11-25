public void process(Node externs, Node root) {
  process(Node.externs(), root); // Utilize the provided method to traverse roots
  if (MAKE_LOCAL_NAMES_UNIQUE) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal t = new NodeTraversal(compiler, renamer);
    t.traverseRoots(externs, root);
  }
  removeDuplicateDeclarations(root);
  new PropogateConstantAnnotations(compiler, assertOnChange)
      .process(externs, root);
}

// Utilize the provided method to traverse roots
public void process(Node externs, Node root) {
  Node externsAndJs = root.getParent();
  Preconditions.checkState(externsAndJs != null);
  Preconditions.checkState(externsAndJs.hasChild(externs));

  NodeTraversal.traverseRoots(
      compiler, Lists.newArrayList(externs, root), this);
}