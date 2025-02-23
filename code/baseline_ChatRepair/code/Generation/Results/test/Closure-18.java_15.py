// Ensuring staleInputs is set even when there are errors if dependency management was tried
try {
  inputs =
      (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
      .manageDependencies(options.dependencyOptions, inputs);
  staleInputs = true;
} catch (CircularDependencyException e) {
  report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
  if (!options.ideMode) { // considering IDE mode can continue parsing
    return null;
  }
} catch (MissingProvideException e) {
  report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
  if (!options.ideMode) { // considering IDE mode can continue parsing
    return null;
  }
}

// Move the check of hasErrors up, to capture and return null if there are errors before more processing happens.
if (hasErrors()) {
    return null;
}

hoistNoCompileFiles();

if (staleInputs) {
  repartitionInputs();
}

// Remaining processing ...

return externAndJsRoot;
