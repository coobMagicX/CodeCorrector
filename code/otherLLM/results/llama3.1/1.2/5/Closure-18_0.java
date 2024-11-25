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
    processAMDAndCommonJSModules();

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
        inputs =
            (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
      } catch (CircularDependencyException e) {
        report(JSError.make(
            JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

        // If in IDE mode, we ignore the error and keep going.
        if (!hasErrors()) {  // Fix: added a check here
          return null;
        }
      } catch (MissingProvideException e) {
        report(JSError.make(
            MISSING_ENTRY_ERROR, e.getMessage()));

        // If in IDE mode, we ignore the error and keep going.
        if (!hasErrors()) {  // Fix: added a check here
          return null;
        }
      }

    processCommonJSModules(); // Added this method call

    if (staleInputs) {
      try {
        inputs =
            (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
      } catch (Exception e) {
        Throwables.propagate(e);
      }
    }

    this.modules = Lists.newArrayList();
    for (CompilerInput input : this.moduleGraph.manageDependencies(
        options.dependencyOptions, inputs)) {
      JSModule module = modulesByInput.get(input);
      if (module != null) {
        this.modules.add(module);
      }
    }
    this.moduleGraph = new JSModuleGraph(this.modules);

    // Add remaining modules to the graph
    for (JSModule module : modules) {
      if (!this.modules.contains(module)) {
        this.moduleGraph.addModule(module);
      }
    }

  } catch (Exception e) {
    Throwables.propagate(e);
  }
  return null;
}

void processCommonJSModules() {
  Map<String, JSModule> modulesByName = Maps.newLinkedHashMap();
  Map<CompilerInput, JSModule> modulesByInput = Maps.newLinkedHashMap();

  for (CompilerInput input : inputs) {
    input.setCompiler(this);
    Node root = input.getAstRoot(this);
    if (root == null) {
      continue;
    }
    ProcessCommonJSModules cjs = new ProcessCommonJSModules(this,
        options.commonJSModulePathPrefix);
    cjs.process(null, root);
    JSModule m = cjs.getModule();
    if (m != null) {
      modulesByName.put(m.getName(), m);
      modulesByInput.put(input, m);
    }
  }

  this.modules = Lists.newArrayList(modulesByName.values());
  this.moduleGraph = new JSModuleGraph(this.modules);

  for (JSModule module : modules) {
    for (CompilerInput input : module.getInputs()) {
      for (String require : input.getRequires()) {
        JSModule dependency = modulesByName.get(require);
        if (dependency == null) {
          report(JSError.make(MISSING_ENTRY_ERROR, require));
        } else {
          module.addDependency(dependency);
        }
      }
    }
  }
}