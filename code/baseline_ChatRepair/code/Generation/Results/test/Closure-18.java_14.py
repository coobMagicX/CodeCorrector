if (options.dependencyOptions.needsManagement() && options.closurePass) {
    try {
        if (moduleGraph == null) {
            moduleGraph = new JSModuleGraph(modules);
        }
        inputs = moduleGraph.manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
    } catch (CircularDependencyException e) {
        report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
        if (!options.ideMode) {
            return null;
        }
    } catch (MissingProvideException e) {
        report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
        if (!options.ideMode) {
            return null;
        }
    }
}

if (staleInputs) {
    repartitionInputs(); // Ensure this method appropriately updates the jsRoot and inputs sequence as needed.
}

// Build the AST.
for (CompilerInput input : inputs) {
    Node n = input.getAstRoot(this);
    if (n == null) { // safeguard if AST root is missing, continue with next input
        continue;
    }

    if (devMode) {
        runSanityCheck();
        if (hasErrors()) {
            return null;
        }
    }

    if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
        // Annotate the nodes in the tree with information from the input file.
        SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
        NodeTraversal.traverse(this, n, sia);
    }

    jsRoot.addChildToBack(n);  // Ensure nodes are always added back to jsRoot
}

if (hasErrors()) {
    return null;
}

return externAndJsRoot;
