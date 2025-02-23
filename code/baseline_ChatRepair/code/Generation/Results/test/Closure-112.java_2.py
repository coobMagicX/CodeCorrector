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
  Map<TemplateType, JSType> typesToReplace = new HashMap<TemplateType, JSType>();
  for (TemplateType key : keys) {
    JSType keyType = inferred.containsKey(key) ? inferred.get(key) : null;
    if (keyType != null) {
      typesToReplace.put(key, keyType);
    } else {
      // Use original type if key is critical, else use UNKNOWN.
      JSType defaultType = isCriticalType(key) ? key : getNativeType(JSTypeNative.UNKNOWN_TYPE);
      typesToReplace.put(key, defaultType);
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

private boolean isCriticalType(TemplateType key) {
    // Determine if a template key is critical to functionality; returns false by default
    return false; // Defaulting false for the simplicity of this example
}
