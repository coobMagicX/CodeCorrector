try {
  // Parse externs sources.
  for (CompilerInput input : externs) {
    Node n = input.getAstRoot(this);
    if (n == null) {
      report(JSError.make("Externs Parse Error", "Failed to obtain AST root for extern: " + input.getName()));
      return null;
    }
    if (hasErrors()) {
      return null;
    }
    externsRoot.addChildToBack(n);
  }

  // Ensure all inputs are available and proper before processing modules and reordering
  if (options.transformAMDToCJSModules || options.processCommonJSModules || options.dependencyOptions.needsManagement()) {
    for (CompilerInput input : inputs) {
      if (input.getAstRoot(this) == null) {
        report(JSError.make("Input Parse Error", "Failed to obtain AST root for input: " + input.getName()));
        return null;
      }
    }
  }

  // Modules inferred in ProcessCommonJS pass.
  if (options.transformAMDToCJSModules || options.processCommonJSModules) {
    processAMDAndCommonJSModules();
  }

  hoistExterns(externsRoot);

  // Check if the sources need to be re-ordered.
  boolean staleInputs = false;
  if (options.dependencyOptions.needsManagement() && options.closurePass) {
    try {
      inputs =
          (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
          .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (CircularDependencyException | MissingProvideException e) {
      report(JSError.make(e instanceof CircularDependencyException ? JSModule.CIRCULAR_DEPENDENCY_ERROR : MISSING_ENTRY_ERROR, e.getMessage()));
      return null;
    }
  }

  hoistNoCompileFiles();

  if (staleInputs) {
    repartitionInputs();
  }
  
  // Followed by the rest of your existing code...
}
