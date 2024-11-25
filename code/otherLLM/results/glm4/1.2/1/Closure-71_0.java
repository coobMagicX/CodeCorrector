private void checkPropertyVisibility(NodeTraversal t,
    Node getprop, Node parent) {
    ObjectType objectType =
        ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
    String propertyName = getprop.getLastChild().getString();

    if (objectType != null) {
        boolean isOverride = t.inGlobalScope() &&
            parent.getType() == Token.ASSIGN &&
            parent.getFirstChild() == getprop;

        JSDocInfo docInfo = findLowestVisibilityObject(objectType, propertyName);
        if (docInfo == null) {
            // If no documentation info found, assume public visibility
            return;
        }

        boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
        Visibility visibility = docInfo.getVisibility();
        JSType ownerType = normalizeClassType(objectType);

        if (isOverride && !sameInput) {
            // Check for private property override in different file
            if (visibility == Visibility.PRIVATE) {
                compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE,
                    objectType.toString()));
            } else if (overridingVisibility != Visibility.INHERITED &&
                       overridingVisibility != visibility) {
                // Check visibility mismatch for non-private properties
                compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH,
                    visibility.name(), objectType.toString(),
                    overridingVisibility.name()));
            }
        } else {
            if (!sameInput && visibility == Visibility.PRIVATE) {
                // If it's a private property access in different file
                if (docInfo.isConstructor() &&
                    isValidPrivateConstructorAccess(parent)) {
                    return;
                }
                compiler.report(t.makeError(getprop, BAD_PRIVATE_PROPERTY_ACCESS,
                    propertyName, validator.getReadableJSTypeName(
                        getprop.getFirstChild(), true)));
            } else if (visibility == Visibility.PROTECTED && (currentClass == null || !currentClass.isSubtype(ownerType))) {
                // If it's a protected property access and not in the same class hierarchy
                compiler.report(t.makeError(getprop, BAD_PROTECTED_PROPERTY_ACCESS,
                    propertyName, validator.getReadableJSTypeName(
                        getprop.getFirstChild(), true)));
            }
        }
    }
}

private JSDocInfo findLowestVisibilityObject(ObjectType objectType, String propertyName) {
    for (; objectType != null; objectType = objectType.getImplicitPrototype()) {
        JSDocInfo docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
        if (docInfo != null && docInfo.getVisibility() != Visibility.INHERITED) {
            return docInfo;
        }
    }
    return null;
}