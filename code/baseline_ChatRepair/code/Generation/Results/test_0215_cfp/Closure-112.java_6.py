private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, we replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();

  // Ensure that the replacement function type is not null and is a function type after replacement
  if (replacementFnType == null || !replacementFnType.isFunctionType()) {
    return false; // Indicate an unsuccessful type replacement if the type is not as expected
  }

  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}
