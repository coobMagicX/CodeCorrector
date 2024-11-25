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
        !options.skipAllPasses() /* Fix: Changed to skipAllPasses */) {

      try {
        this.modules = Lists.newArrayList();
        for (CompilerInput input : inputs) {
          JSModule m = getJSModule(input);
          if (m != null) {
            modules.add(m);
          }
        }
        this.moduleGraph = new JSModuleGraph(this.modules);
        // Reorder the sources based on their dependencies.
        reorderSources();
      } catch (Exception e) {
        Throwables.propagate(e);
      }
    }

    // Annotate the nodes in the tree with information from the input file.
    SourceInformationAnnotator sia =
        new SourceInformationAnnotator(
            /* Fix: Added this line */ input.getName(), options.devMode != DevMode.OFF);
    NodeTraversal.traverse(this, externAndJsRoot, sia);

    if (devMode) {
      runSanityCheck();
      if (hasErrors()) {
        return null;
      }
    }

    jsRoot.addChildToBack(externAndJsRoot);
    return externAndJsRoot;
  } finally {
    stopTracer(tracer, "parseInputs");
  }
}

private JSModule getJSModule(CompilerInput input) {
  // Check if the module is already processed.
  for (JSModule m : modules) {
    if (m.getName().equals(input.getName())) {
      return m;
    }
  }

  // Process the module and add it to the list.
  processAMDAndCommonJSModules();
  JSModule m = null;
  for (JSModule mod : modules) {
    if (mod.getName().equals(input.getName())) {
      m = mod;
      break;
    }
  }
  return m;
}

private void reorderSources() throws Exception {
  // Reorder the sources based on their dependencies.
  Map<String, List<CompilerInput>> dependencies = Maps.newLinkedHashMap();
  for (JSModule module : this.modules) {
    for (String require : input.getRequires()) {
      JSModule dependency = getJSModule(input);
      if (dependency != null) {
        module.addDependency(dependency);
      }
    }
  }

  List<CompilerInput> orderedInputs = Lists.newArrayList();
  for (Map.Entry<String, List<CompilerInput>> entry : dependencies.entrySet()) {
    orderedInputs.addAll(entry.getValue());
  }

  // Update the inputs list with the reordered sources.
  this.inputs = orderedInputs;
}