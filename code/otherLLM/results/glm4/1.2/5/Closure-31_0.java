Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  // If old roots exist (we are parsing a second time), detach each of the
  // individual file parse trees.
  if (externsRoot != null) {
    externsRoot.detachChildren();
  }
  if (jsRoot != null) {
    jsRoot.detachChildren();
  }

  // Parse main js sources.
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
    // Parse externs sources.
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (hasErrors()) {
        return null;
      }
      externsRoot.addChildToBack(n);
    }

    // Modules inferred in ProcessCommonJS pass.
    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    // Check if inputs need to be rebuilt from modules.
    boolean staleInputs = false;

    // Check if the sources need to be re-ordered.
    if (options.dependencyOptions.needsManagement() &&
        !options.skipAllPasses &&
        options.closurePass) {
      for (CompilerInput input : inputs) {
        // Forward-declare all the provided types, so that they
        // are not flagged as errors during dependency resolution.
        forwardDeclareTypes(input);
      }
      try {
        modules = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
        staleInputs = !inputs.equals(modules);
      } catch (Exception e) {
        report(JSError.make(DependencyResolutionError, e.getMessage()));
        return null;
      }
    }

    // Process and add nodes to jsRoot
    for (CompilerInput input : modules) {
      Node root = input.getAstRoot(this);
      if (root == null) {
        continue;
      }
      if (options.transformAMDToCJSModules) {
        new TransformAMDToCJSModule(this).process(null, root);
      }
      if (options.processCommonJSModules) {
        ProcessCommonJSModules cjs = new ProcessCommonJSModules(this,
            options.commonJSModulePathPrefix);
        cjs.process(null, root);
        JSModule m = cjs.getModule();
        if (m != null) {
          modulesByName.put(m.getName(), m);
          modulesByInput.put(input, m);
        }
      }
      jsRoot.addChildToBack(root);
    }

    if (hasErrors()) {
      return null;
    }
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private void forwardDeclareTypes(CompilerInput input) {
  // Implementation to forward declare types for the input.
}