Node parseInputs() {
  ...

  // Moved staleInputs to be outside of the options that check dependencies
  boolean staleInputs = false;

  if (options.dependencyOptions.needsManagement()) {
    if (options.closurePass) {
      for (CompilerInput input : inputs) {
        for (String provide : input.getProvides()) {
          getTypeRegistry().forwardDeclareType(provide);
        }
      }
    }

    try {
      inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
               .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true; // Ensures that inputs are always checked
    } catch (CircularDependencyException e) {
      report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
      return null;
    } catch (MissingProvideException e) {
      report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
      return null;
    }
  }

  if (staleInputs) {
    repartitionInputs();
  }

  // Build the AST.
  for (CompilerInput input : inputs) {
    Node n = input.getAstRoot(this);
    if (n == null) {
      continue;
    }

    if (devMode) {
      runSanityCheck();
      if (hasErrors()) {
        return null;
      }
    }

    jsRoot.addChildToBack(n);
  }

  if (hasErrors()) {
    return null;
  }

  return externAndJsRoot;
}
