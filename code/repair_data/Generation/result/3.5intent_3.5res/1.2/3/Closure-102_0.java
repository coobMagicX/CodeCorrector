public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, this);
    // Move the removal of duplicate declarations before renaming to handle issues correctly
    removeDuplicateDeclarations(root);
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
    }
    new PropogateConstantAnnotations(compiler, assertOnChange)
        .process(externs, root);
}

