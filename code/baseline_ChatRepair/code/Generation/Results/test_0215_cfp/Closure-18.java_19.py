boolean inputsSorted = false;
try {
  if (options.dependencyOptions.needsManagement() && options.closurePass) {
    for (CompilerInput input : inputs) {
      for (String provide : input.getProvides()) {
        getTypeRegistry().forwardDeclareType(provide);
      }
    }

    try {
      inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
              .manageDependencies(options.dependencyOptions, inputs);
      inputsSorted = true;
    } catch (CircularDependencyException e) {
      report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
      if (!options.ideMode) {
        return null;
      }
    } catch (MissingProvideException e) {
      report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
      if (!options.ideMode) {
        return null;
      }
    }
  }

  if (inputsSorted) {
    staleInputs = true;
  }
} catch (Exception e) {
  report(JSError.make("DependencySortingError", e.getMessage()));
  return null; // Failing safely by stopping further execution.
}
