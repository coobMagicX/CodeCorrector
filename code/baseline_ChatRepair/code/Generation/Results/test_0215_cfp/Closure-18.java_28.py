Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  // Previous code snipped for brevity...

  if (options.dependencyOptions.needsManagement() && options.closurePass) {

    // Forward-declare all provided types to avoid flags on being dropped.
    inputs.forEach(input -> input.getProvides().forEach(
        provide -> getTypeRegistry().forwardDeclareType(provide))
    );

    // Dependency Management
    try {
      if (moduleGraph == null) {
        moduleGraph = new JSModuleGraph(modules);
      }
      inputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (CircularDependencyException e) {
      report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
      if (!options.ideMode && hasErrors()) {
        return null;
      }
    } catch (MissingProvideException e) {
      report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
      if (!options.ideMode && hasErrors()) {
        return null;
      }
    }
  }

  hoistNoCompileFiles();

  if (staleInputs) {
    repartitionInputs();
  }

  // Continue with remaining code logic...

  stopTracer(tracer, "parseInputs");
  return externAndJsRoot;
}
