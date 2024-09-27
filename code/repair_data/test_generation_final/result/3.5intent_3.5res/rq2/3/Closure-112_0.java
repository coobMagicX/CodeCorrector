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

  // Check if the inferred types match the template types
  if (!checkTemplateTypesMatch(keys, inferred)) {
    throw new IllegalArgumentException("Template types do not match the expected types");
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}

private boolean checkTemplateTypesMatch(
    ImmutableList<TemplateType> keys, Map<TemplateType, JSType> inferred) {
  for (TemplateType key : keys) {
    if (!inferred.containsKey(key)) {
      return false;
    }
    JSType inferredType = inferred.get(key);
    if (!inferredType.isSubtypeOf(key.getBound())) {
      return false;
    }
  }
  return true;
}