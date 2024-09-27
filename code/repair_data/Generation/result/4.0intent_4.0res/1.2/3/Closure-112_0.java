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

  // If we have compound template types (e.g., T|K), ensure we handle them correctly
  for (Map.Entry<TemplateType, JSType> entry : inferred.entrySet()) {
    TemplateType key = entry.getKey();
    JSType value = entry.getValue();
    if (key.isUnionType()) { // Assuming TemplateType can identify union types
      ImmutableList<TemplateType> subTypes = key.getUnionMembers();
      for (TemplateType subType : subTypes) {
        inferred.put(subType, value); // Assign the resolved type to all union members
      }
    }
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