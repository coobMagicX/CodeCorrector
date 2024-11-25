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

  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  // Only set the JSType if we actually made any changes
  if (replacer.madeChanges) {
    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());
  }

  return replacer.madeChanges;
}