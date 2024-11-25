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

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  if (NodeUtil.isNew(callTarget)) { // Check if the call target is a constructor function
    maybeResolveTemplatedType(
        fnType.getTypeOfThis(),
        getJSType(callTarget.getFirstChild()),
        inferred);
  }

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}