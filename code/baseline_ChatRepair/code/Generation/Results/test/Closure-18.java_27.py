// Check if the sources need to be re-ordered.
boolean staleInputs = false;
if (options.dependencyOptions.needsManagement() && options.closurePass) {
  for (CompilerInput input : inputs) {
    // Forward-declare all the provided types, so that they
    // are not flagged even if they are dropped from the process.
    for (String provide : input.getProvides()) {
      getTypeRegistry().forwardDeclareType(provide);
    }
  }

  try {
    inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
        .manageDependencies(options.dependencyOptions, inputs);
    staleInputs = true;
  } catch (CircularDependencyException e) {
    report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
    
    // If in IDE mode, we ignore the error and keep going.
    if (!isIdeMode() && hasErrors()) {
      return null;
    }
  } catch (MissingProvideException e) {
    report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
    
    // If in IDE mode, we ignore the error and keep going.
    if (!isIdeMode() && hasErrors()) {
      return null;
    }
  }
}

hoistNoCompileFiles();

if (staleInputs) {
  repartitionInputs();
}
