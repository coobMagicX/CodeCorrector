public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, this);

    // Ensure any initial transformations are applied.
    compiler.reportCodeChange();

    if (MAKE_LOCAL_NAMES_UNIQUE) {
        MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
        NodeTraversal t = new NodeTraversal(compiler, renamer);
        t.traverseRoots(externs, root);
        // Ensure changes by the renamer are reported before further processing.
        compiler.reportCodeChange();
    }

    removeDuplicateDeclarations(root);
    
    new PropagateConstantAnnotations(compiler, assertOnChange).process(externs, root);
    // Ensuring all changes are finalized in report.
    compiler.reportCodeChange();
}
