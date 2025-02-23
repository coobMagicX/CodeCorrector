try {
    // Parse externs sources.
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (hasErrors()) {
        return null;
      }
      externsRoot.addChildToBack(n);
    }

    hoistExterns(externsRoot);

    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    // Check if the sources need to be re-ordered.
    boolean staleInputs = false;
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      for (CompilerInput input : inputs) {
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

    hoistNoCompileFiles();

    if (staleInputs) {
      repartitionInputs();
    }

    // Continue with further parsing...
}
