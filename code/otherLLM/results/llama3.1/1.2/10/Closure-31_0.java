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

    // Check if CommonJS modules are being processed
    boolean commonJsProcessed = false;
    for (CompilerInput input : inputs) {
      if (input.getRequires() != null && !input.getRequires().isEmpty()) {
        commonJsProcessed = true;
        break;
      }
    }

    // Check if AMD modules are being processed
    boolean amdProcessed = false;
    for (CompilerInput input : inputs) {
      if (input.getRequires() == null || input.getRequires().isEmpty()) {
        amdProcessed = true;
        break;
      }
    }

    if (commonJsProcessed && options.processCommonJSModules) {
      // Rebuild the inputs from modules
      fillEmptyModules(modules);
      rebuildInputsFromModules();
    } else if (amdProcessed && options.transformAMDToCJSModules) {
      // Rebuild the inputs from modules
      fillEmptyModules(modules);
      rebuildInputsFromModules();
    }

    // Build the AST.
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

      if (options.sourceMapOutputPath != null ||
          options.nameReferenceReportPath != null) {

        // Annotate the nodes in the tree with information from the
        // input file. This information is used to construct the SourceMap.
        SourceInformationAnnotator sia =
            new SourceInformationAnnotator(
                input.getName(), options.devMode != DevMode.OFF);
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