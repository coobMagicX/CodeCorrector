private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      // Immediately report an error if the namespace is never provided
      compiler.report(
          t.makeError(n, MISSING_PROVIDE_ERROR, ns));
    } else {
      JSModule providedModule = provided.explicitModule;

      // Assert that there is an explicit provide as it must be non-null
      Preconditions.checkNotNull(providedModule, "Provided module cannot be null");

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

    // Requires should be removed from the parse tree to not interfere with subsequent runs 
    parent.detachFromParent();
    compiler.reportCodeChange();
  }
}
