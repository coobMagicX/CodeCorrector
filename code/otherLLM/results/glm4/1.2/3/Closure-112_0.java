private boolean inferTemplatedTypesForCall(
    Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);
  if (inferred == null || inferred.isEmpty()) {
    return false; // Early exit if no inference could be made
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType;
  if (fnType instanceof Visitable) {
    replacementFnType = ((Visitable) fnType).visit(replacer)
        .toMaybeFunctionType();
  } else {
    // If the function type does not implement Visitable, we may need to handle it differently
    return false; // Cannot visit non-Visitable types
  }
  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}