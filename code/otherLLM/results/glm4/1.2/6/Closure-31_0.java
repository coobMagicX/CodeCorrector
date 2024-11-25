Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  // ... [other parts of the method] ...

  // Check if the sources need to be re-ordered.
  if (options.dependencyOptions.needsManagement() &&
      !options.skipAllPasses &&
      options.closurePass) {
    List<String> providesList = new ArrayList<>();
    List<String> requiresList = new ArrayList<>();

    for (CompilerInput input : inputs) {
      // Forward-declare all the provided types, so that they
      // are not flagged even if they are dropped from the process.
      for (String provide : input.getProvides()) {
        getTypeRegistry().forwardDeclareType(provide);
        providesList.add(provide);
      }

      for (String require : input.getRequires()) {
        // Add requires to a separate list since we don't forward-declare them
        requiresList.add(require);
      }
    }

    // Sort the provides and requires lists before managing dependencies.
    Collections.sort(providesList);
    Collections.sort(requiresList);

    try {
      inputs =
          (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
          .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (CircularDependencyException e) {
      report(JSError.make(
          JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

      // If in IDE mode, we ignore the error and keep going.
      if (hasErrors()) {
        return null;
      }
    } catch (MissingProvideException e) {
      report(JSError.make(
          MISSING_ENTRY_ERROR, e.getMessage()));

      // If in IDE mode, we ignore the error and keep going.
      if (hasErrors()) {
        return null;
      }
    }
  }

  // ... [rest of the method] ...
}