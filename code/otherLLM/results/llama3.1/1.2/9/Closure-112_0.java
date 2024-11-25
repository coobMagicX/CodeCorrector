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

  // Check if any template type was resolved in the function call
  boolean templateResolved = !inferred.isEmpty();

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  if (templateResolved) {
    // Update JSType and ReturnType only if template types were resolved
    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());
  }

  return replacer.madeChanges;
}