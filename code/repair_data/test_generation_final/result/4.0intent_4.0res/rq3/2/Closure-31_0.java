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

        boolean staleInputs = false;

        // Check if the sources need to be re-ordered.
        if (options.dependencyOptions.needsManagement() &&
            !options.skipAllPasses &&
            options.closurePass) {
            try {
                inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                        .manageDependencies(options.dependencyOptions, inputs);
                staleInputs = true;
            } catch (CircularDependencyException e) {
                report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
                if (hasErrors()) {
                    return null;
                }
            } catch (MissingProvideException e) {
                report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
                if (hasErrors()) {
                    return null;
                }
            }
        }

        for (CompilerInput input : inputs) {
            Node n = input.getAstRoot(this);
            if (n == null) {
                continue;
            }
            if (n.getJSDocInfo() != null) {
                JSDocInfo info = n.getJSDocInfo();
                if (info.isExterns()) {
                    externsRoot.addChildToBack(n);
                    input.setIsExtern(true);
                    input.getModule().remove(input);
                    externs.add(input);
                    staleInputs = true;
                } else if (info.isNoCompile()) {
                    input.getModule().remove(input);
                    staleInputs = true;
                }
            }
        }

        if (staleInputs) {
            fillEmptyModules(modules);
            rebuildInputsFromModules();
        }

        // Build the AST.
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
            if (options.sourceMapOutputPath != null ||
                options.nameReferenceReportPath != null) {
                SourceInformationAnnotator sia =
                        new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
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