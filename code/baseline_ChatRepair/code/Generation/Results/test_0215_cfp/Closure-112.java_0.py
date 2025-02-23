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

  // Replace all template types. If we couldn't find a replacement, and if critical, we keep the original.
  Map<TemplateType, JSType> typesToReplace = new LinkedHashMap<>();
  for (TemplateType key : keys) {
    if (inferred.containsKey(key)) {
      typesToReplace.put(key, inferred.get(key));
    } else {
      // If key is critical to function behavior use original type, else use UNKNOWN.
      // Implement isCriticalType method based on your requirement
      typesToReplace.put(key, isCriticalType(key) ? key : getNativeType(JSTypeNative.UNKNOWN_TYPE));
    }
  }

  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, typesToReplace);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}
