    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      try {
        List<CompilerInput> newInputs =
            (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
        if (!newInputs.equals(inputs)) {
          inputs = newInputs;
          staleInputs = true;
        }
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

    // Additional steps might involve thorough validation and making sure all configurations are met.
    // Ensure no compile or link steps should happen if inputs are stale or dependencies unmet.
    if (staleInputs) {
      repartitionInputs();
    }
