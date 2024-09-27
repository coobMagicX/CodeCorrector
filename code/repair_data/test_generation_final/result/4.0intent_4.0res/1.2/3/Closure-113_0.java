private void processRequireCall(NodeTraversal t, Node n, Node parent) {
    Node left = n.getFirstChild();
    Node arg = left.getNext();
    if (verifyIsLast(t, left, arg)) {  // Ensures that 'arg' is the last argument
        String ns = arg.getString();
        ProvidedName provided = providedNames.get(ns);
        if (provided == null || !provided.isExplicitlyProvided()) {
            // Reporting an error if the namespace is not provided properly
            compiler.report(
                t.makeError(arg, MISSING_PROVIDE_ERROR, ns));
        } else {
            JSModule providedModule = provided.explicitModule;

            // Check that the module is not null, as it should be explicitly provided
            Preconditions.checkNotNull(providedModule);

            JSModule module = t.getModule();
            if (moduleGraph != null &&
                module != providedModule &&
                !moduleGraph.dependsOn(module, providedModule)) {
                // Reporting cross-module dependency errors
                compiler.report(
                    t.makeError(n, XMODULE_REQUIRE_ERROR, ns,
                        providedModule.getName(),
                        module.getName()));
            }
        }

        maybeAddToSymbolTable(left);
        maybeAddStringNodeToSymbolTable(arg);

        // Detach and report code change if the namespace is provided
        if (provided != null) {
            parent.detach();
            compiler.reportCodeChange();
        }
    }
}