private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types based on call arguments
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, we
  // mark them as unknown which later need to be checked if they remain in the type template.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();
  FunctionType replacementFnType = fnType.substituteGenerics(inferred).toMaybeFunctionType();

  if (replacementFnType == null || !allTemplateTypesResolved(keys, inferred)) {
    // If not all template types resolved or invalid replacement type, relax to original type but proceed.
    replacementFnType = fnType;
  }

  Preconditions.checkNotNull(replacementFnType, "Replacement FunctionType cannot be null");

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}

private boolean allTemplateTypesResolved(
        ImmutableList<TemplateType> keys, Map<TemplateType, JSType> inferred) {
    return keys.stream()
           .noneMatch(key -> inferred.getOrDefault(key, JSTypeNative.UNKNOWN_TYPE).isUnknownType());
}
