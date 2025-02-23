try {
    inputs =
        (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
        .manageDependencies(options.dependencyOptions, inputs);
    staleInputs = true;
} catch (CircularDependencyException e) {
    report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

    // Check if we should stop execution in non-IDE mode.
    if (!options.devMode.equals(DevMode.IDE)) { 
        return null;
    }
} catch (MissingProvideException e) {
    report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));

    // Check if we should stop execution in non-IDE mode.
    if (!options.devMode.equals(DevMode.IDE)) {
        return null;
    }
}

// Continue with the usual processing if no critical errors appeared (depends on the flag setup).
if (hasErrors() && !options.devMode.equals(DevMode.IDE)) {
    return null;
}
