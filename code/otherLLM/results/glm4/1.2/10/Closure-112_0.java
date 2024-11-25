private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    // If no template types exist or cannot be resolved, return false.
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = 
      inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, skip setting types.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType;
  if (replacer.madeChanges) {
    replacementFnType = fnType.visit(replacer)
        .toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);

    // Only set the types if a valid replacement was found and used.
    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());
  }

  return replacer.madeChanges;
}