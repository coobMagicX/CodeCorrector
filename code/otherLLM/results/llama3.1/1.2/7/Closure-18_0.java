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

  // Parse main JS sources.
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

    hoistExterns(externsRoot);

    // Check if the sources need to be re-ordered.
    boolean staleInputs = false;
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      for (CompilerInput input : inputs) {
        // Forward-declare all the provided types, so that they
        // are not flagged even if they are dropped from the process.
        for (String provide : input.getProvides()) {
          getTypeRegistry().forwardDeclareType(provide);
        }
      }

      try {
        Collections.sort(inputs); // Sort inputs to fix dependency sorting issue

        inputs =
            (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
      } catch (CircularDependencyException e) {
        report(JSError.make(
            JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

        // If in IDE mode, we ignore the error and keep going.
        if (hasErrors()) {
          return null;
        }
      } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        // If in IDE mode, we ignore the error and keep going.
        if (hasErrors()) {
          return null;
        }
      }
    }

    try {
      modules = Lists.newArrayList();
      for (CompilerInput input : this.moduleGraph.manageDependencies(
          options.dependencyOptions, inputs)) {
        JSModule dependency = moduleGraph.getModule(input);
        if (dependency != null) {
          modules.add(dependency);
        }
      }
      this.modules = modules;
      this.moduleGraph = new JSModuleGraph(modules);
    } catch (Exception e) {
      Throwables.propagate(e);
    }

    try {
      modulesByInput = Maps.newLinkedHashMap();
      for (JSModule module : modules) {
        for (CompilerInput input : module.getInputs()) {
          modulesByInput.put(input, module);
        }
      }
    } catch (Exception e) {
      Throwables.propagate(e);
    }

    // Ensure that each module's dependencies are added correctly
    try {
      for (JSModule module : modules) {
        for (CompilerInput input : module.getInputs()) {
          JSModule dependency = modulesByInput.get(input);
          if (dependency == null) {
            report(JSError.make(MISSING_ENTRY_ERROR, input.getName()));
          } else {
            module.addDependency(dependency);
          }
        }
      }
    } catch (Exception e) {
      Throwables.propagate(e);
    }

    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}