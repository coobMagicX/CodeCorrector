private void processRequireCall(NodeTraversal t, Node n, Node parent) {
  Node left = n.getFirstChild();
  Node arg = left.getNext();
  if (verifyLastArgumentIsString(t, left, arg)) {
    String ns = arg.getString();
    ProvidedName provided = providedNames.get(ns);
    
    // Check why unrecognizedRequires is being populated despite explicit provide
    boolean shouldReportError = false;
    if (provided == null || !provided.isExplicitlyProvided()) {
      shouldReportError = true;  // Set flag to report error for missing provides
    } else {
      JSModule providedModule = provided.explicitModule;

      Preconditions.checkNotNull(providedModule);

      JSModule module = t.getModule();
      if (moduleGraph != null &&
          module != providedModule &&
          !moduleGraph.dependsOn(module, providedModule)) {
        shouldReportError = true;  // Set flag to report error for incorrect requires
      }
    }

    if (shouldReportError) {  
      unrecognizedRequires.add(
          new UnrecognizedRequire(n, ns, t.getSourceName()));
    } else {
      JSModule module = t.getModule();
      
      // Requires should be removed before further processing.
      // Some clients run closure pass multiple times, first with
      // the checks for broken requires turned off. In these cases, we
      // allow broken requires to be preserved by the first run to
      // let them be caught in the subsequent run.
      maybeAddToSymbolTable(left);
      maybeAddStringNodeToSymbolTable(arg);

      if (provided != null) {
        parent.detachFromParent();
        compiler.reportCodeChange();
      }
    }
  }
}