private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types from parameters
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, we replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  // Update the JSType of the call target node with the inferred type from replacer
  if (callTarget != null) {
    callTarget.setJSType(replacementFnType);
  }

  // Set the return type of the current node to match the return type of the replacement function type
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}