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

  boolean staleInputs = false;
  
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

    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        ModuleGraph moduleGraph = this.moduleGraph;
        if (moduleGraph == null) {
          moduleGraph = new JSModuleGraph(modules);
        }
        ImmutableList<CompilerInput> managedInputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
        inputs = managedInputs;
        staleInputs = true;
      } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
        if (!options.ideMode) {
          return null;
        }
      } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        return null;
      }
    }

    hoistNoCompileFiles();

    if (staleInputs) {
      repartitionInputs();
    }

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
      
      if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
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
