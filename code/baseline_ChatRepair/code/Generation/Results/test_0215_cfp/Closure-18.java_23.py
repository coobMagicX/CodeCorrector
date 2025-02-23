Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  externsRoot = IR.block();
  externsRoot.setIsSyntheticBlock(true);

  jsRoot = IR.block();
  jsRoot.setIsSyntheticBlock(true);

  externAndJsRoot = IR.block(externsRoot, jsRoot);
  externAndJsRoot.setIsSyntheticBlock(true);

  if (options.tracer.isOn()) {
    tracker = new PerformanceTracker(jsRoot, options.tracer);
    addChangeHandler(tracker.getCodeChangeHandler());
  }

  Tracer tracer = newTracer("parseInputs");

  boolean errorsOccurred = false;

  try {
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (n == null || hasErrors()) {
        errorsOccurred = true;
        break;
      }
      externsRoot.addChildToBack(n);
    }

    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                 .manageDependencies(options.dependencyOptions, inputs);
      } catch (CircularDependencyException | MissingProvideException e) {
        report(JSError.make(e.getClass().getSimpleName(), e.getMessage()));
        errorsOccurred = true;
      }
    }

    if (!errorsOccurred) {
      for (CompilerInput input : inputs) {
        Node n = input.getAstRoot(this);
        if (n == null || hasErrors()) {
          errorsOccurred = true;
          break;
        }
        jsRoot.addChildToBack(n);
      }
    }
  } finally {
    stopTracer(tracer, "parseInputs");
  }

  if (errorsOccurred || hasErrors()) {
    return null;
  } else {
    return externAndJsRoot;
  }
}
