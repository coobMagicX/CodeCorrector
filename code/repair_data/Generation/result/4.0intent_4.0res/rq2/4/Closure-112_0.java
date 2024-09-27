private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
    final ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap()
        .getTemplateKeys();
    if (keys.isEmpty()) {
        return false;
    }

    // Try to infer the template types
    Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

    // Before replacing, check if the inferred types match the expected types
    for (Map.Entry<TemplateType, JSType> entry : inferred.entrySet()) {
        TemplateType template = entry.getKey();
        JSType expectedType = fnType.getTemplateTypeMap().getResolvedType(template);
        JSType actualType = entry.getValue();

        // Throw an error if the actual type does not match the expected type
        if (!actualType.isSubtypeOf(expectedType)) {
            throw new RuntimeException("Type mismatch: Expected " + expectedType + " but got " + actualType);
        }
    }

    // Replace all template types. If we couldn't find a replacement, we
    // replace it with UNKNOWN.
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
    Node callTarget = n.getFirstChild();

    FunctionType replacementFnType = fnType.visit(replacer)
        .toMaybeFunctionType();
    Preconditions.checkNotNull(replacementFnType);

    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    return replacer.madeChanges;
}