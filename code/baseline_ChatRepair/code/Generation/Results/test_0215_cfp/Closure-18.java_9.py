Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  //Same initial steps...

  Tracer tracer = newTracer("parseInputs");
  try {
    // Same externs parsing...

    hoistExterns(externsRoot);

    // Check if the sources need to be re-ordered.
    boolean staleInputs = false;
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        List<CompilerInput> sortedInputs = new JSModuleGraph(modules).manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
        inputs = sortedInputs;
      } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
        if (hasErrors()) {
          return null;
        }
      } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        if (hasErrors()) {
          return null;
        }
      }
    }

    // Resuming remaining methodology for hoisting, input repartitioning and building AST...

    // Return the created ASTs both externs and js concatenated.
    if (hasErrors()) {
      return null;
    }
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}
