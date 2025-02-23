private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Infer the template types based on function call parameters
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Check and handle cases where inference may fail to find suitable types, fallback to unknown.
  if (inferred.isEmpty()) {
    // Assign UNKNOWN type to template type positions unable to be inferred
    for (TemplateType key : keys) {
      inferred.put(key, getNativeType(JSTypeNative.UNKNOWN_TYPE));
    }
  }

  // Replace all template types in the function type. 
  // If no suitable replacements, use UNKNOWN to maintain structural conformity.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();
  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();

  // Ensure a valid replacement function type is always used, fallback to unknown if null
  if (replacementFnType == null) {
    replacementFnType = (FunctionType) getNativeType(JSTypeNative.UNKNOWN_TYPE);
  }

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}
