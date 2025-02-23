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

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  // Apply the replacer, ensuring the function type can be validly transformed.
  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  if (replacementFnType == null) {
    // Handle case where the function type after replacing templates could not be correctly formed.
    return false;
  }

  // Check all template types are replaced
  for (TemplateType key : keys) {
    if (inferred.getOrDefault(key, registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)) ==
        registry.getNativeType(JSTypeNative.UNKNOWN_TYPE)) {
      // A template key remains un-replaced, returning as failure.
      return false;
    }
  }

  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}
