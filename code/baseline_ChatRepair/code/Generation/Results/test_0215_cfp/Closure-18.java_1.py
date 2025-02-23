Node parseInputs() {
  boolean devMode = options.devMode != DevMode.OFF;

  resetRoots();

  parseExternsSources();

  if (options.transformAMDToCJSModules || options.processCommonJSModules) {
    processAMDAndCommonJSModules();
  }

  hoistExterns(externsRoot);

  manageDependencies();

  hoistNoCompileFiles();

  // Repartition inputs if needed.
  if (staleInputs) {
    repartitionInputs();
  }

  return buildAST(devMode);
}

private void resetRoots() {
  // Detach children of existing roots if re-parsing
  if (externsRoot != null) {
    externsRoot.detachChildren();
  }
  if (jsRoot != null) {
    jsRoot.detachChildren();
  }
  // Initialize new root nodes for externs and js
  externsRoot = IR.block();
  externsRoot.setIsSyntheticBlock(true);
  jsRoot = IR.block();
  jsRoot.setIsSyntheticBlock(true);
  externAndJsRoot = IR.block(externsRoot, jsRoot);
  externAndJsRoot.setIsSyntheticBlock(true);
}

private void parseExternsSources() {
  Tracer tracer = newTracer("parseExternsSources");
  try {
    for (CompilerInput input : externs) {
      Node n = input.getAstRoot(this);
      if (n == null || hasErrors()) {
        return;
      }
      externsRoot.addChildToBack(n);
    }
  } finally {
    stopTracer(tracer, "parseExternsSources");
  }
}

private void manageDependencies() {
  Tracer tracer = newTracer("manageDependencies");
  try {
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        moduleGraph = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph);
        inputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
      } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
      } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
      }
    }
  } finally {
    stopTracer(tracer, "manageDependencies");
  }
}

private Node buildAST(boolean devMode) {
  Tracer tracer = newTracer("buildAST");
  try {
    for (CompilerInput input : inputs) {
      Node n = input.getAstRoot(this);
      if (n == null) continue;
      annotateAndAddToJsRoot(n, input, devMode);
    }
    return hasErrors() ? null : externAndJsRoot;
  } finally {
    stopTracer(tracer, "buildAST");
  }
}

private void annotateAndAddToJsRoot(Node n, CompilerInput input, boolean devMode) {
  if (devMode) {
    runSanityCheck();
    if (hasErrors()) {
      return;
    }
  }
  if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
    SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
    NodeTraversal.traverse(this, n, sia);
  }
  jsRoot.addChildToBack(n);
}
