try {
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
        for (CompilerInput input : inputs) {
            // Forward-declare all the provided types.
            for (String provide : input.getProvides()) {
                getTypeRegistry().forwardDeclareType(provide);
            }
        }

        try {
            JSModuleGraph moduleGraphTemp = (moduleGraph == null) ? new JSModuleGraph(modules) : moduleGraph;
            inputs = moduleGraphTemp.manageDependencies(options.dependencyOptions, inputs);
            staleInputs = true;
        } catch (CircularDependencyException e) {
            report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
            if (!options.ideMode) {
                return null; // Stop further processing in non-IDE mode
            }
        } catch (MissingProvideException e) {
            report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
            if (!options.ideMode) {
                return null; // Stop further processing in non-IDE mode
            }
        }
    }

    hoistNoCompileFiles();

    if (staleInputs) {
        repartitionInputs();
    }
} catch (Exception e) {
    report(JSError.make("GenericError", e.getMessage()));
    return null;
}
