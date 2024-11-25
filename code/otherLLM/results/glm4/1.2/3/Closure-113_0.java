private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  
  // Ensure that 'arg' is not null before proceeding
  if (arg != null && verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;
      
      // This must be non-null, because there was an explicit provide
      if (providedModule == null) {
        throw new IllegalStateException("Provided module cannot be null.");
      }

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

    // Requires should be removed before further processing
    parent.detachFromParent();
    compiler.reportCodeChange();
  }
}