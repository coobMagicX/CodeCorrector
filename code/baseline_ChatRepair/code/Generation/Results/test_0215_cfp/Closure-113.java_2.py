private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);

    if (provided == null) {
      // If the provided object is null, then the namespace has never been provided.
      compiler.report(
          t.makeError(n, JSC_MISSING_PROVIDE_ERROR, ns));
    } else if (!provided.isExplicitlyProvided()) {
      // If the provided object is not explicitly provided then it's a late provision error.
      compiler.report(
          t.makeError(n, JSC_LATE_PROVIDE_ERROR, ns));
    } else {
      JSModule providedModule = provided.explicitModule;
      Preconditions.checkNotNull(providedModule, "Provided module cannot be null");

      JSModule module = t.getModule();
      if (moduleGraph != null && module != providedModule &&
          !moduleGraph.dependsOn(module, providedModule)) {
        compiler.report(
            t.makeError(n, XMODULE_REQUIRE_ERROR, ns,
                        providedModule.getName(),
                        module.getName()));
      }
    }

    maybeAddToSymbolTable(left);
    maybeAddStringNodeToSymbolTable(arg);

    // Always remove the goog.require immediately to prevent multiple handling in subsequent runs
    parent.detachFromParent();
    compiler.reportCodeChange();
  }
}
