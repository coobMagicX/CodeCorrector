private void processRequireCall(NodeTraversal t, Node n, Node parent) {
    Node left = n.getFirstChild();
    Node arg = left.getNext();
    if (verifyLastArgumentIsString(t, left, arg)) {
        String ns = arg.getString();
        // Check for namespaces that are implicitly available such as 'goog'
        if (ns.startsWith("goog")) {
            maybeAddToSymbolTable(left);  // Ensure 'goog' is added to the symbol table
            maybeAddStringNodeToSymbolTable(arg);
            return; // Skip further processing for Google Closure Library namespaces
        }
        ProvidedName provided = providedNames.get(ns);
        if (provided == null || !provided.isExplicitlyProvided()) {
            unrecognizedRequires.add(
                new UnrecognizedRequire(n, ns, t.getSourceName()));
        } else {
            JSModule providedModule = provided.explicitModule;

            // This must be non-null, because there was an explicit provide.
            Preconditions.checkNotNull(providedModule);

            JSModule module = t.getModule();
            if (moduleGraph != null &&
                module != providedModule &&
                !moduleGraph.dependsOn(module, providedModule)) {
                compiler.report(
                    t.makeError(n, XMODULE_REQUIRE_ERROR, ns,
                        providedModule.getName(),
                        module.getName()));
            }
        }

        maybeAddToSymbolTable(left);
        maybeAddStringNodeToSymbolTable(arg);

        // Requires should be removed before further processing.
        if (provided != null) {
            parent.detachFromParent();
            compiler.reportCodeChange();
        }
    }
}