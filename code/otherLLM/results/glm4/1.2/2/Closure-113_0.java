private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  
  // Ensure that 't' and 'n' are not null to prevent NullPointerExceptions.
  if (t == null || n == null) {
    throw new IllegalArgumentException("NodeTraversal or node must not be null");
  }
  
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    
    // Ensure that 'ns' is not null to prevent NullPointerExceptions.
    if (ns == null) {
      throw new IllegalArgumentException("Namespace string cannot be null");
    }
    
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;
      
      // Check for null to avoid NullPointerExceptions.
      if (providedModule == null) {
        throw new IllegalStateException("Provided module cannot be null");
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

    // Requires should be removed before further processing.
    if (provided != null) {
      parent.detachFromParent();
      compiler.reportCodeChange();
    }
  } else {
    // If the last argument is not a string, we might have an error that needs to be reported or handled.
    compiler.report(t.makeError(n, "Invalid require call", "The last argument of require must be a string."));
  }
}