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
        try {
            if (moduleGraph == null) {
                moduleGraph = new JSModuleGraph(modules);
            }
            inputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
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

    hoistNoCompileFiles();

    if (staleInputs) {
        repartitionInputs();
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
