private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      // Reporting error for unrecognized namespace
      compiler.report(
          t.makeError(n, UNRECOGNIZED_REQUIRE_ERROR, ns));
    } else {
      JSModule providedModule = provided.explicitModule;
      Preconditions.checkNotNull(providedModule, "Provided module cannot be null if explicitly provided.");

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

    // Managing node removal based on whether the namespace was recognized, avoiding unintended removal
    if (provided != null) {
      parent.detachFromParent();
      compiler.reportCodeChange();
    }
  }
}
