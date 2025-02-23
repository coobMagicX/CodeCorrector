Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  if (externsRoot != null) {
    externsRoot.detachChildren();
  }
  if (jsRoot != null) {
    jsRoot.detachChildren();
  }

  jsRoot = IR.block();
  jsRoot.setIsSyntheticBlock(true);

  externsRoot = IR.block();
  externsRoot.setIsSyntheticBlock(true);

  externAndJsRoot = IR.block(externsRoot, jsRoot);
  externAndJsRoot.setIsSyntheticBlock(true);

  if (options.tracer.isOn()) {
    tracker = new PerformanceTracker(jsRoot, options.tracer);
    addChangeHandler(tracker.getCodeChangeHandler());
  }

  Tracer tracer = newTracer("parseInputs");

  try {
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (hasErrors()) {
        return null;
      }
      externsRoot.addChildToBack(n);
    }

    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    hoistExterns(externsRoot);

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
      } catch (CircularDependencyException | MissingProvideException e) {
        report(JSError.make(e instanceof CircularDependencyException
                            ? JSModule.CIRCULAR_DEPENDENCY_ERROR : MISSING_ENTRY_ERROR, e.getMessage()));

        if (!options.ideMode) {  // Ensure the IDE mode check is correctly applied for both exception cases.
          return null;
        }
      }
    }

    hoistNoCompileFiles();

    if (staleInputs) {
      repartitionInputs();
    }

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

      if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
        NodeTraversal.traverse(this, n, sia);
      }

      jsRoot.addChildToBack(n);
    }

    if (hasErrors() && !options.ideMode) {
      return null;
    }
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}
