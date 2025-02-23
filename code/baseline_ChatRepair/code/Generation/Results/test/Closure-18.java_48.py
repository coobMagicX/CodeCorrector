Node parseInputs() {
    boolean devMode = options.devMode != DevMode.OFF;

    // Detach children from previous parses if they exist
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
        // Parse externs sources
        for (CompilerInput input : externs) {
            Node n = input.getAstRoot(this);
            if (hasErrors()) return null;
            externsRoot.addChildToBack(n);
        }

        hoistExterns(externsRoot);

        // Process modules if necessary
        if (options.transformAMDToCJSModules || options.processCommonJSModules) {
            processAMDAndCommonJSModules();
        }

        boolean staleInputs = false;
        if (options.dependencyOptions.needsManagement() && options.closurePass) {
            manageDependencies();
            staleInputs = true;
        }

        hoistNoCompileFiles();

        if (staleInputs) {
            repartitionInputs();
        }

        // Build the AST from JS source inputs
        for (CompilerInput input : inputs) {
            Node n = input.getAstRoot(this);
            if (n == null) continue;

            if (devMode) {
                runSanityCheck();
                if (hasErrors()) return null;
            }

            annotateSourceIfNeeded(input, n);

            jsRoot.addChildToBack(n);
        }

        if (hasErrors()) return null;
        return externAndJsRoot;
    } finally {
        stopTracer(tracer, "parseInputs");
    }
}

private void manageDependencies() {
    for (CompilerInput input : inputs) {
        for (String provide : input.getProvides()) {
            getTypeRegistry().forwardDeclareType(provide);
        }
    }
    try {
        inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                .manageDependencies(options.dependencyOptions, inputs);
    } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
        if (hasErrors()) throw new IllegalStateException(e.getMessage());
    } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        if (hasErrors()) throw new IllegalStateException(e.getMessage());
    }
}

private void annotateSourceIfNeeded(CompilerInput input, Node n) {
    if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
        NodeTraversal.traverse(this, n, sia);
    }
}
