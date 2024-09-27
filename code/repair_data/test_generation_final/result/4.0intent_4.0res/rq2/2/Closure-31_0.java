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
        cleanUpAfterErrors();
        return null;
      }
      externsRoot.addChildToBack(n);
    }

    if (options.transformAMDToCJSModules || options.processCommonJSModules) {
      processAMDAndCommonJSModules();
    }

    boolean staleInputs = false;

    if (options.dependencyOptions.needsManagement() && !options.skipAllPasses && options.closurePass) {
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
        report(JSError.make(e instanceof CircularDependencyException ?
            JSModule.CIRCULAR_DEPENDENCY_ERROR : MISSING_ENTRY_ERROR, e.getMessage()));
        if (hasErrors()) {
          cleanUpAfterErrors();
          return null;
        }
      }
    }

    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null) {
        continue;
      }
      if (n.getJSDocInfo() != null && processNode(n, input)) {
        staleInputs = true;
      }
    }

    if (staleInputs) {
      fillEmptyModules(modules);
      rebuildInputsFromModules();
    }

    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null) {
        continue;
      }

      if (devMode) {
        runSanityCheck();
        if (hasErrors()) {
          cleanUpAfterErrors();
          return null;
        }
      }

      annotateSource(n, input);

      jsRoot.addChildToBack(n);
    }

    if (hasErrors()) {
      cleanUpAfterErrors();
      return null;
    }
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private void cleanUpAfterErrors() {
  jsRoot.detachChildren();
  externsRoot.detachChildren();
  externAndJsRoot.detachChildren();
}

private boolean processNode(Node n, CompilerInput input) {
  JSDocInfo info = n.getJSDocInfo();
  if (info.isExterns()) {
    externsRoot.addChildToBack(n);
    input.setIsExtern(true);
    input.getModule().remove(input);
    externs.add(input);
    return true;
  } else if (info.isNoCompile()) {
    input.getModule().remove(input);
    return true;
  }
  return false;
}

private void annotateSource(Node n, CompilerInput input) {
  if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
    SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
    NodeTraversal.traverse(this, n, sia);
  }
}