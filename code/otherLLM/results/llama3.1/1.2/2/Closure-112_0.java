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

  // Handle nested functions correctly and verify template type resolution for constructors
  Node callTarget = n.getFirstChild();
  if (NodeUtil.isFunctionCall(callTarget)) {
    FunctionType replacementFnType = fnType.visit(new TemplateTypeReplacer(
        registry, inferred)).toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);

    // Resolve template types for constructor functions
    if (replacementFnType.getParameters().size() == 1) {
      Node child = callTarget.getFirstChild();
      if (NodeUtil.isGet(child)) {
        Node obj = child.getFirstChild();
        maybeResolveTemplatedType(
            replacementFnType.getTypeOfThis(),
            getJSType(obj),
            inferred);
      }
    }

    // Replace all template types. If we couldn't find a replacement, we
    // replace it with UNKNOWN.
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(
        registry, inferred);
    Node newCallTarget = callTarget.visit(replacer).toMaybeNode();
    Preconditions.checkNotNull(newCallTarget);

    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    return replacer.madeChanges;
  } else {
    // Handle non-function calls
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(
        registry, inferred);
    Node newCallTarget = callTarget.visit(replacer).toMaybeNode();
    Preconditions.checkNotNull(newCallTarget);

    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    return replacer.madeChanges;
  }
}