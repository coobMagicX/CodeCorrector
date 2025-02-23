    try {
      // Manage dependencies
      if (options.dependencyOptions.needsManagement() && options.closurePass) {
        try {
          inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
                   .manageDependencies(options.dependencyOptions, inputs);
          staleInputs = true;
        } catch (CircularDependencyException e) {
          report(JSError.make(JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

          if (hasErrors()) {
            return null;  // Ensure early exit if errors exist
          }
        } catch (MissingProvideException e) {
          report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));

          if (hasErrors()) {
            return null;  // Ensure early exit if errors exist
          }
        }
      }
      
      // Check and handle stale inputs
      if (staleInputs) {
        if (!repartitionInputs()) { // This is a method check handling if repartition was successful
          report(JSError.make("Input re-partitioning failed"));
          return null;
        }
      }
      
      // Parse main JS sources.
      for (CompilerInput input : inputs) {
        Node n = input.getAstRoot(this);
        if (n == null) {
          continue;
        }

        if (devMode) {
          runSanityCheck();
          if (hasErrors()) {
            return null;  // Return early if errors exist
          }
        }

        if (options.sourceMapOutputPath != null || options.nameReferenceReportPath != null) {
          SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), devMode);
          NodeTraversal.traverse(this, n, sia);
        }

        jsRoot.addChildToBack(n);
      }

      if (hasErrors()) {
        return null;  // Check again for errors after processing all inputs
      }
      
      return externAndJsRoot;
      
    } finally {
      stopTracer(tracer, "parseInputs");
    }
