    // Checking for dependency management
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
        try {
            inputs =
                (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                .manageDependencies(options.dependencyOptions, inputs);
            staleInputs = true;
        } catch (CircularDependencyException e) {
            report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));
            // If not in IDE mode, we must return from the function to avoid further processing of inconsistent state.
            if (!options.ideMode) {
                return null;
            }
        } catch (MissingProvideException e) {
            report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
            // If not in IDE mode, we must return from the function to avoid further processing of inconsistent state.
            if (!options.ideMode) {
                return null;
            }
        }
    }
