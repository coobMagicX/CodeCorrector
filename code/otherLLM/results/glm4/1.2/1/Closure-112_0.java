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
    return false; // Early exit if no types were inferred
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);
  
  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();
  if (replacementFnType == null) {
    return false; // Early exit if the replacement function type is null
  }

  Node callTarget = n.getFirstChild();
  if (callTarget != null && NodeUtil.isCall(callTarget)) {
    updateBind(n); // Ensure that bind information is updated before setting types
  }

  callTarget.setJSType(replacementFnType);
  n.setJSType(replacementFnType.getReturnType());

  return replacer.madeChanges;
}

private void updateBind(Node n) {
  CodingConvention.Bind bind =
      compiler.getCodingConvention().describeFunctionBind(n, true);
  if (bind == null) {
    return;
  }

  FunctionType callTargetFn = getJSType(bind.target)
      .restrictByNotNullOrUndefined().toMaybeFunctionType();
  if (callTargetFn == null) {
    return;
  }

  n.setJSType(
      callTargetFn.getBindReturnType(
          // getBindReturnType expects the 'this' argument to be included.
          bind.getBoundParameterCount() + 1));
}

private Map<TemplateType, JSType> inferTemplateTypesFromParameters(
    FunctionType fnType, Node call) {
  if (fnType.getTemplateTypeMap().getTemplateKeys().isEmpty()) {
    return Collections.emptyMap();
  }

  Map<TemplateType, JSType> resolvedTypes = Maps.newIdentityHashMap();

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