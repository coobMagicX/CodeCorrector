  try {
    inputs =
        (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
        .manageDependencies(options.dependencyOptions, inputs);
    staleInputs = true;
  } catch (CircularDependencyException e) {
    report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

    // Instead of checking hasErrors here, continue under IDE mode
    if (!options.devMode){  // Assuming devMode is used to indicate non-strict mode like IDE
      return null;
    }
  } catch (MissingProvideException e) {
    report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));

    // Instead of checking hasErrors here, continue under IDE mode
    if (!options.devMode){  // Assuming devMode is used to indicate non-strict mode
      return null;
    }
  }

  if (hasErrors() && options.devMode) {
    return null;
  }
