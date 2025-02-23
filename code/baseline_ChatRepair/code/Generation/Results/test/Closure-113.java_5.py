private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // Check if the name is provided and explicitly provided
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(new UnrecognizedRequire(n, ns, t.getSourceName()));
     
      // Report an error since the namespace is unrecognized or not explicitly provided
      compiler.report(t.makeError(n, "MISSING_PROVIDE_ERROR", ns));
     
      // Detach parent only if necessary, depending on compiler configurations or requirements.
      parent.detachFromParent();
      compiler.reportCodeChange();
    } else {
      JSModule providedModule = provided.explicitModule;
      Preconditions.checkNotNull(providedModule); // this should be non-null since it's explicitly provided

      JSModule module = t.getModule();
      if (moduleGraph != null && module != providedModule && !moduleGraph.dependsOn(module, providedModule)) {
        // Report cross-module dependency error
        compiler.report(
          t.makeError(n, "XMODULE_REQUIRE_ERROR", ns, providedModule.getName(), module.getName()));
      }
      
      // Operation on successful requirement check
      maybeAddToSymbolTable(left);
      maybeAddStringNodeToSymbolTable(arg);
    }
  }
}
