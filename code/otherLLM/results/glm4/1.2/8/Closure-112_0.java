private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
  final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
  if (keys.isEmpty()) {
    return false;
  }

  // Try to infer the template types
  Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

  // Replace all template types. If we couldn't find a replacement, we replace it with UNKNOWN.
  TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
  Node callTarget = n.getFirstChild();

  FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();
  Preconditions.checkNotNull(replacementFnType);

  // Ensure that the template types are replaced in all children of the node
  Node current = callTarget;
  while (current != null) {
    if (!NodeUtil.isGet(current)) { // Skip GET nodes, as they are handled separately
      replacementFnType = replaceTemplateTypesInFunctionType(replacementFnType, replacer);
      current.setJSType(replacementFnType);
      if (current instanceof FunctionDeclaration || current instanceof FunctionExpression) {
        current.setReturnType(replacementFnType.getReturnType());
      }
    }
    current = current.getNextSibling();
  }

  return replacer.madeChanges;
}

// Helper method to replace template types in a given FunctionType
private FunctionType replaceTemplateTypesInFunctionType(FunctionType fnType, TemplateTypeReplacer replacer) {
  Map<TemplateType, JSType> replacements = Maps.newHashMap(replacer.getReplacementMap());
  for (JSType type : fnType.getParameters().values()) {
    if (type instanceof TemplateType) {
      TemplateType templateType = (TemplateType) type;
      JSType replacement = replacements.getOrDefault(templateType, UNKNOWN);
      replacements.put(templateType, replacement);
    }
  }

  // Create a new FunctionType with replaced template types
  return fnType.replaceTypes(replacements);
}