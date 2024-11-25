private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // Ensure that the variable 'provided' is not null and is explicitly provided
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;

      // Ensure that the 'providedModule' is not null to avoid NullPointerException
      if (providedModule == null) {
        compiler.report(t.makeError(n, XMODULE_PROVIDED_ERROR, "Provided module cannot be null for explicitly provided name: " + ns));
        return;
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

    // Remove the require call to avoid processing it further
    parent.detachFromParent();
    compiler.reportCodeChange();
  }
}