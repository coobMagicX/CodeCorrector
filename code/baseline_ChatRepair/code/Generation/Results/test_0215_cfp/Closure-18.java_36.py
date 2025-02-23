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
            manageDependencies();  // Assuming this method encapsulates managing dependencies and error handling properly
            if (hasErrors()) {
                return null;  // Early exit if dependency management fails
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
            if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
                SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
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
