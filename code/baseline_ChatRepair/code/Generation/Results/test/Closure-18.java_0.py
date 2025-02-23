Node parseInputs() {
    boolean devMode = options.devMode != DevMode.OFF;

    // Clear existing trees if re-parsing
    if (externsRoot != null) {
        externsRoot.detachChildren();
    }
    if (jsRoot != null) {
        jsRoot.detachChildren();
    }

    // Create the root nodes
    jsRoot = IR.block();
    jsRoot.setIsSyntheticBlock(true);
    externsRoot = IR.block();
    externsRoot.setIsSyntheticBlock(true);
    externAndJsRoot = IR.block(externsRoot, jsRoot);
    externAndJsRoot.setIsSyntheticBlock(true);

    // Setup performance tracker if tracing
    if (options.tracer.isOn()) {
        tracker = new PerformanceTracker(jsRoot, options.tracer);
        addChangeHandler(tracker.getCodeChangeHandler());
    }

    Tracer tracer = newTracer("parseInputs");
    boolean staleInputs = false;

    try {
        // Processing extern sources
        for (CompilerInput input : externs) {
            Node n = input.getAstRoot(this);
            if (hasErrors()) return null;
            externsRoot.addChildToBack(n);
        }
        
        // Process modules
        if (options.transformAMDToCJSModules || options.processCommonJSModules) {
            processAMDAndCommonJSModules();
        }

        hoistExterns(externsRoot);

        // Manage dependencies if required
        if (options.dependencyOptions.needsManagement() && options.closurePass) {
            try {
                manageDependencies();
                staleInputs = true;
            } catch (CircularDependencyException | MissingProvideException e) {
                handleDependencyErrors(e);
                if (hasErrors()) return null;
            }
        }

        hoistNoCompileFiles();

        // Re-partition files if needed
        if (staleInputs) {
            repartitionInputs();
        }

        // Build the AST from input files
        for (CompilerInput input : inputs) {
            Node n = input.getAstRoot(this);
            if (n == null) continue;
            if (devMode) {
                runSanityCheck();
                if (hasErrors()) return null;
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

private void manageDependencies() throws CircularDependencyException, MissingProvideException {
    inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
}

private void handleDependencyErrors(Exception e) {
    report(JSError.make(
            e instanceof CircularDependencyException ? JSModule.CIRCULAR_DEPENDENCY_ERROR : MISSING_ENTRY_ERROR,
            e.getMessage()));
}
