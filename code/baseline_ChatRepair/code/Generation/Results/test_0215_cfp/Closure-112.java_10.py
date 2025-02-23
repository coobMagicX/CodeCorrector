private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
    final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
    if (keys.isEmpty()) {
        return false;
    }

    // Attempt to infer the template types based on the current context
    Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

    // Ensure all keys have been successfully inferred and are not unknown
    for (TemplateType key : keys) {
        JSType type = inferred.get(key);
        if (type == null || type.isUnknownType()) {
            return false;  // Cannot successfully infer all types, or specific type is unknown
        }
    }

    // Replace all template types with inferred values
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
    Node callTarget = n.getFirstChild();

    FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();
    
    // Check that the replacement type is not null
    if (replacementFnType == null) {
        throw new IllegalStateException("Replacement function type should not be null");
    }

    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    return replacer.madeChanges;
}
