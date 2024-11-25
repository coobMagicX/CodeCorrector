private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // Check if the provided name is null or not explicitly provided
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;
      
      // Ensure that the explicit module is non-null
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

    // Remove the require call before further processing
    parent.detachFromParent();
    compiler.reportCodeChange();
  }
}