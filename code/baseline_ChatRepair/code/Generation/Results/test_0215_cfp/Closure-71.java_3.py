private void checkPropertyVisibility(NodeTraversal t, Node getprop, Node parent) {
    ObjectType objectType = ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
    String propertyName = getprop.getLastChild().getString();

    if (objectType != null) {
        boolean isOverride = t.inGlobalScope() && parent.getType() == Token.ASSIGN && parent.getFirstChild() == getprop;

        // Navigating up the prototype chain to find the respective owner of the property.
        objectType = objectType.getImplicitPrototype();
        
        JSDocInfo docInfo = null;
        for (; objectType != null; objectType = objectType.getImplicitPrototype()) {
            docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
            if (docInfo != null) {
                break;
            }
        }

        if (docInfo == null) {
            // If no documentation could be found, it's presumed public.
            return;
        }

        boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
        Visibility visibility = docInfo.getVisibility();

        if (isOverride) {
            JSDocInfo overridingInfo = parent.getJSDocInfo();
            if (visibility == Visibility.PRIVATE && (!sameInput || !isOwnerAccessible(t, objectType))) {
                compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, propertyName));
            } else if (overridingInfo != null) {
                Visibility overridingVisibility = overridingInfo.getVisibility();
                if (overridingVisibility != Visibility.INHERITED && overridingVisibility != visibility) {
                    compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, propertyName, visibility.name(), overridingVisibility.name()));
                }
            }
        } // Other access checks remain unchanged
    }
}

private boolean isOwnerAccessible(NodeTraversal t, ObjectType objectType) {
    if (objectType == null) return false;
    String definingClass = objectType.toString();
    String currentFile = t.getInput().getName();
    String currentClass = t.getScope().getRootNode().getQualifiedName();

    // Possibly adjust logic based on how classes and files are exactly associated in your specific environment/context.
    return definingClass.equals(currentClass) && objectType.isDefinedIn(currentFile);
}
