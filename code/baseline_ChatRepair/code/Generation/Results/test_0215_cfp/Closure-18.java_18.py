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
                return null;
            }
            externsRoot.addChildToBack(n);
        }

        if (options.transformAMDToCJSModules || options.processCommonJSModules) {
            processAMDAndCommonJSModules();
        }

        hoistExterns(externsRoot);

        if (options.dependencyOptions.needsManagement() && options.closurePass) {
            DependencyOptions depOptions = options.dependencyOptions;
            JSModuleGraph moduleGraph = new JSModuleGraph(modules);
            try {
                List<CompilerInput> managedInputs = moduleGraph.manageDependencies(depOptions, inputs);
                if (!inputs.equals(managedInputs)) {
                    inputs = managedInputs; // Use updated inputs
                    repartitionInputs();
                }
            } catch (CircularDependencyException | MissingProvideException e) {
                report(JSError.make(e.getMessage()));
                if (notInDevMode()) {
                    return null;
                }
            }
        }

        hoistNoCompileFiles();

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
            if (needsSourceMapInfo()) {
                annotateSourceInformation(input, n);
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
