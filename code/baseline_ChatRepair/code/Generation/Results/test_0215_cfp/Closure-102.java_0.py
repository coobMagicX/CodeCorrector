public void process(Node externs, Node root) {
    // First remove duplicate declarations before making names unique
    removeDuplicateDeclarations(root);

    // Traverse the AST and apply transformations
    NodeTraversal.traverse(compiler, root, this);

    // Conditionally make local names unique if the option is enabled
    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        // Traverse both external and root nodes to ensure all scopes are accounted for
        t.traverseRoots(externs, root);
    }

    // Apply other transformations after local name uniqueness to avoid interference
    new PropagateConstantAnnotations(compiler, assertOnChange).process(externs, root);
}
