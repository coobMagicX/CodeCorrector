private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  
  // Check if the last argument is a string as expected by verifyLastArgumentIsString
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // If provided is null or not explicitly provided, report an error
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
    // Some clients run closure pass multiple times, first with
    // the checks for broken requires turned off. In these cases, we
    // allow broken requires to be preserved by the first run to
    // let them be caught in the subsequent run.
    
    // Check if the provided name is null or not explicitly provided
    if (provided != null && !provided.isExplicitlyProvided()) {
      parent.detachFromParent();
      compiler.reportCodeChange();
    } else {
      // If it's explicitly provided, do nothing and let further processing continue
    }
  }
}