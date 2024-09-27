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

  // Check if the inferred types match the expected template types
  for (TemplateType key : keys) {
    if (!inferred.containsKey(key)) {
      throw new RuntimeException("Template type not inferred: " + key);
    }
    JSType inferredType = inferred.get(key);
    JSType expectedType = fnType.getTemplateTypeMap().get(key);
    if (!inferredType.equals(expectedType)) {
      throw new RuntimeException("Invalid template type: " + key + ", expected: " + expectedType + ", inferred: " + inferredType);
    }
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