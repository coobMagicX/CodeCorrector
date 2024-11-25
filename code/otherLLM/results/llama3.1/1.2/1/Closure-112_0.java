private boolean inferTemplatedTypesForCall(
    Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = 
      inferTemplateTypesFromParameters(fnType, n);

  // Check for null handling in case of unexpected conditions
  Preconditions.checkNotNull(inferred);
  
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  if (replacementFnType == null) {
    // Handle the case where replacementFnType is null
    return false;
  }
  
  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}