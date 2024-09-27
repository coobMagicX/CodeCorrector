private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
      .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Validate the inferred types against the expected generic types
  for (TemplateType templateType : keys) {
    if (!inferred.containsKey(templateType)) {
      // Handle case where type inference fails for template type
      // Log an error or throw an exception as desired
      System.err.println("Failed to infer type for template type: " + templateType);
      return false;
    }
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}

private Map<TemplateType, JSType> inferTemplateTypesFromParameters(FunctionType fnType, Node call) {
  if (fnType.getTemplateTypeMap().getTemplateKeys().isEmpty()) {
    return Collections.emptyMap();
  }

  Map<TemplateType, JSType> resolvedTypes = Maps.newIdentityHashMap();

  Node callTarget = call.getFirstChild();
  if (NodeUtil.isGet(callTarget)) {
    Node obj = callTarget.getFirstChild();
    maybeResolveTemplatedType(fnType.getTypeOfThis(), getJSType(obj), resolvedTypes);
  }

  if (call.hasMoreThanOneChild()) {
    Node arguments = call.getChildAtIndex(1);
    maybeResolveTemplateTypeFromNodes(fnType.getParameters(), arguments.siblings(), resolvedTypes);
  }

  return resolvedTypes;
}