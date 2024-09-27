private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
    .getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  if (inferred == null) {
    throw new IllegalStateException("Failed to infer template types from parameters");
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
    Node paramNode = call.getChildAtIndex(1).getNext();
    for (Node param : paramNode.children()) {
      if (param.isFunction()) {
        JSType expectedType = fnType.getParameterType(paramNode);
        if (!param.getJSType().isEquivalentTo(expectedType)) {
          throw new IllegalArgumentException("Parameter type mismatch");
        }
      }
    }
    maybeResolveTemplateTypeFromNodes(fnType.getParameters(), paramNode.siblings(), resolvedTypes);
  }
  return resolvedTypes;
}