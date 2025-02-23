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
      if (n == null || hasErrors()) {
        continue; // Change here to continue processing other inputs.
      }
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
      } catch (CircularDependencyException | MissingProvideException e) {
        reportError(e);  // Consolidate error handling.
      }
    }

    hoistNoCompileFiles();

    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null || hasErrors()) {
        continue;
      }

      if (devMode) {
        runSanityCheck();
        if (hasErrors()) {
          continue; // Allows continued processing to gather more issues.
        }
      }

      if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
        NodeTraversal.traverse(this, n, sia);
      }

      jsRoot.addChildToBack(n);
    }

    if (hasErrors()) {
      return null;
    }
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private void reportError(Exception e) {
    report(JSError.make(e.getMessage()));
    if (!options.ideMode && hasErrors()) {
        return null;  // Stop processing if not in IDE mode and errors exist.
    }
}
