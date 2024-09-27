Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  // Detach children from previous roots if they exist.
  if (externsRoot != null) {
    externsRoot.detachChildren();
  }
  if (jsRoot != null) {
    jsRoot.detachChildren();
  }

  // Initialize the main JS and externs source blocks.
  jsRoot = IR.block();
  jsRoot.setIsSyntheticBlock(true);

  externsRoot = IR.block();
  externsRoot.setIsSyntheticBlock(true);

  externAndJsRoot = IR.block(externsRoot, jsRoot);
  externAndJsRoot.setIsSyntheticBlock(true);

  // Setup performance tracking if enabled.
  if (options.tracer.isOn()) {
    tracker = new PerformanceTracker(jsRoot, options.tracer);
    addChangeHandler(tracker.getCodeChangeHandler());
  }

  Tracer tracer = newTracer("parseInputs");

  try {
    // Parse externs sources and handle any errors.
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (n == null || hasErrors()) {
        return null;
      }
      externsRoot.addChildToBack(n);
    }

    // Process modules if required by the options.
    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    // Hoist externs if necessary.
    hoistExterns(externsRoot);

    // Dependency management and reordering if needed.
    boolean staleInputs = false;
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      for (CompilerInput input : inputs) {
        for (String provide : input.getProvides()) {
          getTypeRegistry().forwardDeclareType(provide);
        }
      }

      try {
        inputs = manageDependencies();
        staleInputs = true;
      } catch (CircularDependencyException | MissingProvideException e) {
        report(JSError.make(e.getErrorType(), e.getMessage()));
        if (hasErrors()) {
          return null;
        }
      }
    }

    // Hoist non-compile files.
    hoistNoCompileFiles();

    // Repartition inputs if they are stale.
    if (staleInputs) {
      repartitionInputs();
    }

    // Build the AST from JS files.
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

      // Annotate source information for source maps if needed.
      if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
        NodeTraversal.traverse(this, n, sia);
      }

      jsRoot.addChildToBack(n);
    }

    // Return null if there are errors after processing all inputs.
    if (hasErrors()) {
      return null;
    }

    // Return the combined root of externs and JS.
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}