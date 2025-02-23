Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  // Detach previous parse trees if they exist
  if (externsRoot != null) externsRoot.detachChildren();
  if (jsRoot != null) jsRoot.detachChildren();

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

  boolean staleInputs = false;
  Tracer tracer = newTracer("parseInputs");

  try {
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (hasErrors()) return null;
      externsRoot.addChildToBack(n);
    }

    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    hoistExterns(externsRoot);

    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
      } catch (Exception e) {
        reportDependencyError(e);
        if (!isInIDE()) {
          return null;  // Ensure we exit if not in IDE mode after a serious error
        }
      }
    }

    hoistNoCompileFiles();

    if (staleInputs) {
      repartitionInputs();
    }

    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null) continue;
      
      if (devMode) {
        runSanityCheck();
        if (hasErrors()) return null;
      }

      annotateForSourceMap(input, n);

      jsRoot.addChildToBack(n);
    }

    if (hasErrors()) return null;
    
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private void reportDependencyError(Exception e) {
  if (e instanceof CircularDependencyException) {
    report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
  } else if (e instanceof MissingProvideException) {
    report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
  }
}

private boolean isInIDE() {
  return options.devMode == DevMode.OFF;
}

private void annotateForSourceMap(CompilerInput input, Node n) {
  if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
    SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
    NodeTraversal.traverse(this, n, sia);
  }
}
