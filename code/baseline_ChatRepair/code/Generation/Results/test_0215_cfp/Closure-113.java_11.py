private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    if (provided == null || !provided.isExplicitlyProvided()) {
      // Report the error using a presumed DiagnosticType for missing provides.
      // This requires an appropriate DiagnosticType to be defined, such as MISSING_PROVIDE_ERROR.
      // Replace 'MISSING_PROVIDE_ERROR' with the actual constant if it's different.
      JSError error = JSError.make(n, DiagnosticGroups.MISSING_PROVIDE_ERROR, ns);
      compiler.report(t.makeError(n, error));
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule providedModule = provided.explicitModule;
      Preconditions.checkNotNull(providedModule);

      JSModule module = t.getModule();
      if (moduleGraph != null &&
          module != providedModule &&
          !moduleGraph.dependsOn(module, providedModule)) {
        JSError error = JSError.make(n, DiagnosticGroups.XMODULE_REQUIRE_ERROR, ns, providedModule.getName(), module.getName());
        compiler.report(t.makeError(n, error));
      }
    }

    maybeAddToSymbolTable(left);
    maybeAddStringNodeToSymbolTable(arg);

    // Remove the require if the namespace was valid (didn't add to unrecognizedRequires)
    if (provided != null && (provided == null || !provided.isExplicitlyProvided())) {
      parent.detach();
      compiler.reportCodeChange();
    }
  }
}
