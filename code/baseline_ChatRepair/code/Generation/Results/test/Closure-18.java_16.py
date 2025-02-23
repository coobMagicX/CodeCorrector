// Check if the sources need to be re-ordered.
boolean staleInputs = false;
if (options.dependencyOptions.needsManagement() && options.closurePass) {
  try {
    inputs = (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
             .manageDependencies(options.dependencyOptions, inputs);
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

hoistNoCompileFiles();

if (staleInputs) {
  repartitionInputs();
}

if (hasErrors()) {
    return null;
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
    SourceInformationAnnotator sia = new SourceInformationAnnotator(input.getName(), options.devMode != DevMode.OFF);
    NodeTraversal.traverse(this, n, sia);
  }

  jsRoot.addChildToBack(n);
}

if (hasErrors()) {
  return null;
}
return externAndJsRoot;
