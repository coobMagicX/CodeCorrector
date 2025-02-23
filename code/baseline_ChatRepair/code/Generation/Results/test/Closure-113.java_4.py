private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      unrecognizedRequires.add(new UnrecognizedRequire(n, ns, t.getSourceName()));

      // Use another existing error code if UNRECOGNIZED_REQUIRE_ERROR is undefined.
      compiler.report(t.makeError(n, XMODULE_REQUIRE_ERROR, ns));
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
    if (provided != null) {
      parent.detachFromParent();
      compiler.reportCodeChange();
    }
  }
}
