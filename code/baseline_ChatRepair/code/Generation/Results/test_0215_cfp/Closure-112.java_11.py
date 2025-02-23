private boolean inferTemplatedTypesForCall(Node n, FunctionType fnType) {
    ImmutableList<TemplateType> keys = fnType.getTemplateTypeMap().getTemplateKeys();
    if (keys.isEmpty()) {
        return false;  // If there are no template keys, return false as no inference needed.
    }

    // Inferring the template types using the provided arguments and parameters
    Map<TemplateType, JSType> inferred = inferTemplateTypesFromParameters(fnType, n);

    // Check if the inferred types fully and correctly cover all the template keys,
    // and none of the inferred types should be UNKNOWN 
    // (unless the original template explicitly allows it).
    for (TemplateType key : keys) {
        JSType inferredType = inferred.get(key);
        if (inferredType == null || inferredType.isUnknownType()) {
            return false;
        }
    }

    // Replace the template types using the inferred types
    TemplateTypeReplacer replacer = new TemplateTypeReplacer(registry, inferred);
    Node callTarget = n.getFirstChild();

    // Visiting the function type using the replacer to attempt to generate a new function type
    FunctionType replacementFnType = fnType.visit(replacer).toMaybeFunctionType();

    // Ensure that the process of visiting does not result in a null function type. This should not happen typically.
    if (replacementFnType == null) {
        throw new IllegalStateException("Generated replacement function type should not be null");
    }

    // Apply the inferred function type to the call target and set the return type accordingly
    callTarget.setJSType(replacementFnType);
    n.setJSType(replacementFnType.getReturnType());

    // Return true if changes were made during the replacement process
    return replacer.madeChanges();
}
