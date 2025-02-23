private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // Check if the namespace is never provided.
    if (provided == null) {
      compiler.report(
          t.makeError(n, REQUIRE_NAMESPACE_NEVER_PROVIDED, ns));
      return; // Early return to stop further processing.
    }

    if (!provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;
      Preconditions.checkNotNull(providedModule, "Provided module should not be null.");

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

    // Remove the require if the provided is valid.
    if (provided.isExplicitlyProvided()) {
      parent.detach();
      compiler.reportCodeChange();
    }
  }
}
