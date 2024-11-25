private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types from parameters
  Map<TemplateType, JSType> inferred = 
      inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType;
  if (callTarget != null) {
    replacementFnType = fnType.visit(replacer)
        .toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);

    // Set the JS type of the call target and the return type of the function
    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    // Return whether changes were made
    return replacer.madeChanges;
  }

  return false; // Handle case where callTarget is null to prevent NPE
}