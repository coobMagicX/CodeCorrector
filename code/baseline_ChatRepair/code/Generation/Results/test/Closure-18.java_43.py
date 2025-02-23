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
            if (n == null || hasErrors()) {
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
                inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                        .manageDependencies(options.dependencyOptions, inputs);
            } catch (CircularDependencyException | MissingProvideException e) {
                report(JSError.make(e.getErrorType(), e.getMessage()));
                return null;
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

            annotateNode(n, input);

            jsRoot.addChildToBack(n);
        }

        if (hasErrors()) {
            return null;
        }
    } finally {
        stopTracer(tracer, "parseInputs");
    }

    return externAndJsRoot;
}

private void annotateNode(Node n, CompilerInput input) {
    if (options.sourceMapOutputPath != null ||
        options.nameReferenceReportPath != null) {

        SourceInformationAnnotator sia =
            new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
        NodeTraversal.traverse(this, n, sia);
    }
}
