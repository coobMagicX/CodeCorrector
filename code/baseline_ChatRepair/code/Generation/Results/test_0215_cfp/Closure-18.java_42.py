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

    boolean inputsChanged = false;
    if (options.dependencyOptions.needsManagement()) {
      try {
        JSModuleGraph moduleGraph = (this.moduleGraph == null) ? new JSModuleGraph(modules) : this.moduleGraph;
        inputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
        inputsChanged = true;
      } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
        return null;
      } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        return null;
      }
    }

    if (inputsChanged) {
      repartitionInputs();
    }

    hoistNoCompileFiles();

    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null) {
        continue;
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
