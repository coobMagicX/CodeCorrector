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
    return false; // Early exit if no types could be inferred
  }

  // Replace all template types. If we couldn't find a replacement, we
  // replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(
      registry, inferred);

  Node callTarget = n.getFirstChild();
  FunctionType replacementFnType = fnType.visit(replacer)
      .toMaybeFunctionType();

  if (replacementFnType == null) {
    return false; // Early exit if the visit returned null
  }

  // Update types on the node and its children
  replaceTemplateTypesOnNode(n, replacementFnType);

  return replacer.madeChanges;
}

private void replaceTemplateTypesOnNode(Node n, FunctionType newFnType) {
  Node current = n.getFirstChild();
  while (current != null) {
    if (!NodeUtil.isGet(current)) { // Skip nodes that are part of property access
      JSType currentJSType = current.getJSType();
      current.setJSType(newFnType.getTypeForVariable(current.getName()));
      replaceTemplateTypesOnNode(current, newFnType);
    }
    current = NodeUtil.getNextChild(current);
  }

  // Update the node's type and return type
  n.setJSType(newFnType);
}