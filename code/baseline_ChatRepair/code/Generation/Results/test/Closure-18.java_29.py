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
    try {
      manageDependenciesForInputs();
      
      staleInputs = true;
    } catch (CircularDependencyException | MissingProvideException e) {
      reportErrorDependingOnMode(e);
    }

    hoistNoCompileFiles();

    if (staleInputs) {
      repartitionInputs();
    }

    for (CompilerInput input : inputs) {
      Node node = parseAndCheckForErrors(input);
      if (node == null) continue;
      annotateSourceInformation(input, node);
      jsRoot.addChildToBack(node);
    }

    if (hasErrors()) {
      return null;
    }
    
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private void manageDependenciesForInputs() throws CircularDependencyException, MissingProvideException {
  if (options.dependencyOptions.needsManagement() && options.closurePass) {
    for (CompilerInput input : inputs) {
      for (String provide : input.getProvides()) {
        getTypeRegistry().forwardDeclareType(provide);
      }
    }
    inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph).manageDependencies(options.dependencyOptions, inputs);
  }
}

private void reportErrorDependingOnMode(Exception e) {
  if (options.ideMode) {
    reportWarning(e.getMessage());
  } else {
    reportError(e.getMessage());
    throw e;
  }
}

private Node parseAndCheckForErrors(CompilerInput input) {
  Node n = input.getAstRoot(this);
  if (hasErrors()) {
    return null;
  }
  if (devMode) {
    runSanityCheck();
    if (hasErrors()) {
      return null;
    }
  }
  return n;
}

private void annotateSourceInformation(CompilerInput input, Node n) {
  if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
    SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
    NodeTraversal.traverse(this, n, sia);
  }
}
