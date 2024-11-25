private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    // Check for undeclared or non-provided modules in a way that is compatible with non-strict mode
    if (provided == null || (!provided.isExplicitlyProvided() && !isModuleDeclaredLocally(t, ns))) {
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
    if (provided != null) {
      parent.detachFromParent();
      compiler.reportCodeChange();
    } else if (!isModuleDeclaredLocally(t, ns)) {
      // If the module is not declared locally, we may want to preserve it for future runs
      t.addPreserveNode(n);
    }
  }
}

// Helper method to check if a module has been declared in the local scope
private boolean isModuleDeclaredLocally(NodeTraversal t, String moduleName) {
  return t.findFirstReference(moduleName) != null;
}