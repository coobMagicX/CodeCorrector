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

  // Check if any of the inferred types are erroneous or missing a replacement and replace with UNKNOWN
  for (Map.Entry<TemplateType, JSType> entry : inferred.entrySet()) {
    if (entry.getValue() == null || !entry.getValue().isSubtypeOf(fnType.getTemplateTypeMap().getResolvedTemplateType(entry.getKey()))) {
      inferred.put(entry.getKey(), registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
    }
  }

  // Replace all template types using the inferred or default UNKNOWN types.
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