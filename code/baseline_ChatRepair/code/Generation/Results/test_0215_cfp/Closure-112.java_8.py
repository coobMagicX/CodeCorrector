private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Infer the template types from the parameters of the function call.
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Ensures that every required template type has a resolved or fallback type.
  for (TemplateType key : keys) {
    inferred.putIfAbsent(key, registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
  }

  // Create a replacer with inferred template types, replacing unresolved types with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();
  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();

  // Double-check that replacement function type is not null and valid.
  if (replacementFnType == null) {
    callTarget.setJSType(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
    n.setJSType(registry.getNativeType(JSTypeNative.UNKNOWN_TYPE));
    return false;
  }

  // Set the types on the appropriate nodes
  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges; // return the status indicating if any types were actually changed
}
