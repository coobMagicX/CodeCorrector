private boolean inferTemplatedTypesForCall(
    Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Create an initial lattice to estimate the types
  FlowScope scope = createInitialEstimateLattice();

  // Try to infer the template types from parameters
  Map<TemplateType, JSType> inferred = 
      inferTemplateTypesFromParameters(fnType, n);

  if (inferred.isEmpty()) {
    return false; // No types could be inferred, so we cannot proceed
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType;
  if (callTarget != null) {
    // Visit the node to potentially replace template types
    Node visitedNode = callTarget.visit(replacer);
    
    // Convert the possibly updated node back to a function type
    replacementFnType = visitedNode.toMaybeFunctionType();
  } else {
    // If no child exists, return false as we can't infer a type for an empty node
    return false;
  }

  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}