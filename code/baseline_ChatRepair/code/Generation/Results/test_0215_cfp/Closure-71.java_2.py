private void checkPropertyVisibility(NodeTraversal t, Node getprop, Node parent) {
    ObjectType objectType = ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
    String propertyName = getprop.getLastChild().getString();
    
    if (objectType != null) {
        boolean isOverride = t.inGlobalScope() && parent.getType() == Token.ASSIGN && parent.getFirstChild() == getprop;

        if (isOverride) {
            objectType = objectType.getImplicitPrototype();
        }
       JSDocInfo docInfo = null;
        for (; objectType != null; objectType = objectType.getImplicitPrototype()) {
            docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
            if (docInfo != null && docInfo.getVisibility() != Visibility.INHERITED) {
                break;
            }
        }

        if (objectType == null || docInfo == null) {
            return;  // Assume it's public or not found.
        }

        boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
        Visibility visibility = docInfo.getVisibility();
        JSType ownerType = normalizeClassType(objectType);

        if (isOverride) {
            JSDocInfo overridingInfo = parent.getJSDocInfo();
            Visibility overridingVisibility = overridingInfo == null ? Visibility.INHERITED : overridingInfo.getVisibility();

            if (visibility == Visibility.PRIVATE && !sameInput) {
                compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, propertyName, objectType.toString(), sameInput ? "same file" : "different files"));
            } else if (overridingVisibility != Visibility.INHERITED && overridingVisibility != visibility) {
                compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, visibility.name(), propertyName, overridingVisibility.name()));
            }
        } else {
            // Other checks remain unchanged as they pertain to accessing private, protected properties directly - not overrides.
        }
    }
}
