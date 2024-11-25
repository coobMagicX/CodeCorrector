private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;

      // Ensure that 'providedModule' is not null to avoid NPE
      if (providedModule == null) {
        compiler.report(t.makeError(n, XMODULE_REQUIRE_ERROR, ns,
            "null", module.getName())); // Use a placeholder name for the module in error
      } else {
        JSModule module = t.getModule();
        if (moduleGraph != null &&
            module != providedModule &&
            !moduleGraph.dependsOn(module, providedModule)) {
          compiler.report(t.makeError(n, XMODULE_REQUIRE_ERROR, ns,
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
}