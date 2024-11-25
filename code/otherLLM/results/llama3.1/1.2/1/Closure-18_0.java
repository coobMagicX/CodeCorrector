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

  // Parse main JS sources.
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

    hoistExterns(externsRoot);

    // Check if the sources need to be re-ordered.
    boolean staleInputs = false;
    if (options.dependencyOptions.needsManagement() && options.closurePass) {
      for (CompilerInput input : inputs) {
        // Forward-declare all the provided types, so that they
        // are not flagged even if they are dropped from the process.
        for (String provide : input.getProvides()) {
          getTypeRegistry().forwardDeclareType(provide);
        }
      }

      try {
        inputs =
            (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
            .manageDependencies(options.dependencyOptions, inputs);
        staleInputs = true;
      } catch (CircularDependencyException e) {
        report(JSError.make(
            JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

        // If in IDE mode, we ignore the error and keep going.
        if (hasErrors()) {
          return null;
        }
      } catch (MissingProvideException e) {
        report(JSError.make(
            MISSING_ENTRY_ERROR, e.getMessage()));

        // If in IDE mode, we ignore the error and keep going.
        if (hasErrors()) {
          return null;
        }
      }
    }

    try {
      for (CompilerInput input : inputs) {
        JSType jsType = getDeclaredTypeInAnnotation(input.getSourceName(), input.getNode(), input.getJSDocInfo());
        // Handle the case where jsType is not null
        if (jsType != null) {
          // Add your logic here to handle jsType
        }
      }
    } catch (Exception e) {
      report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
    }

    try {
      inputs =
          (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
          .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (CircularDependencyException e) {
      report(JSError.make(
          JSModule.CIRCULAR_DEPENDENCY_ERROR, e.getMessage()));

      // If in IDE mode, we ignore the error and keep going.
      if (hasErrors()) {
        return null;
      }
    }

    try {
      inputs =
          (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
          .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (MissingProvideException e) {
      report(JSError.make(
          MISSING_ENTRY_ERROR, e.getMessage()));

      // If in IDE mode, we ignore the error and keep going.
      if (hasErrors()) {
        return null;
      }
    }

  } finally {
    try {
      inputs =
          (moduleGraph == null ? new JSModuleGraph(modules) : moduleGraph)
          .manageDependencies(options.dependencyOptions, inputs);
      staleInputs = true;
    } catch (Exception e) {
      report(JSError.make(MISSING_ENTRY_ERROR, e.getMessage()));
    }
  }

  return externAndJsRoot;
}

private JSType getDeclaredTypeInAnnotation(String sourceName,
        Node node, JSDocInfo info) {
  JSType jsType = null;
  Node objNode =
      node.isGetProp() ? node.getFirstChild() :
      NodeUtil.isObjectLitKey(node, node.getParent()) ? node.getParent() :
      null;
  if (info != null) {
    if (info.hasType()) {
      jsType = info.getType().evaluate(scope, typeRegistry);
    } else if (FunctionTypeBuilder.isFunctionTypeDeclaration(info)) {
      String fnName = node.getQualifiedName();
      jsType = createFunctionTypeFromNodes(
          null, fnName, info, node);
    }
  }
  return jsType;
}