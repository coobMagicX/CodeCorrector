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

  // Check if all template types were successfully inferred
  boolean allInferred = keys.stream().allMatch(key -> inferred.containsKey(key) && !inferred.get(key).isUnknownType());

  if (!allInferred) {
    return false;  // Return early as we can't successfully substitute all types
  }

  // Replace all template types with the inferred values
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();
  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();

  Preconditions.checkNotNull(replacementFnType, "Replacement function type should not be null");

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}
