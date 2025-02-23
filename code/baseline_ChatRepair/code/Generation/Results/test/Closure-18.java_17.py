if (options.dependencyOptions.needsManagement() && options.closurePass) {
    for (CompilerInput input : inputs) {
        for (String provide : input.getProvides()) {
            getTypeRegistry().forwardDeclareType(provide);
        }
    }

    try {
        List<CompilerInput> newInputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                                        .manageDependencies(options.dependencyOptions, inputs);
        if (!inputs.equals(newInputs)) {
            inputs = newInputs;
            staleInputs = true;
        }
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

    if (staleInputs) {
        repartitionInputs();
    }
}
