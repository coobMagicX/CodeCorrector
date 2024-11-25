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

  if (inferred == null || inferred.isEmpty()) {
    return false; // If no types were inferred, there's nothing to replace.
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}

private Map<TemplateType, JSType> inferTemplateTypesFromParameters(
    FunctionType fnType, Node call) {
  if (fnType.getTemplateTypeMap().getTemplateKeys().isEmpty()) {
    return Collections.emptyMap();
  }

  Map<TemplateType, JSType> resolvedTypes = Maps.newHashMap(); // Use HashMap to allow modifications

  Node callTarget = call.getFirstChild();
  if (NodeUtil.isGet(callTarget)) {
    Node obj = callTarget.getFirstChild();
    maybeResolveTemplatedType(
        fnType.getTypeOfThis(),
        getJSType(obj),
        resolvedTypes);
  }

  if (call.hasMoreThanOneChild()) {
    maybeResolveTemplateTypeFromNodes(
        fnType.getParameters(),
        call.getChildAtIndex(1).siblings(),
        resolvedTypes);
  }
  return resolvedTypes;
}