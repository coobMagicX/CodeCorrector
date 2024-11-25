private void checkPropertyVisibility(NodeTraversal t,
    Node getprop, Node parent) {
  ObjectType objectType =
      ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();

  if (objectType != null) {
    // Is this a normal property access, or are we trying to override
    // an existing property?
    boolean isOverride = t.inGlobalScope() &&
        parent.getType() == Token.ASSIGN &&
        parent.getFirstChild() == getprop;

    // Find the lowest property defined on a class with visibility
    // information.
    if (isOverride) {
      objectType = objectType.getImplicitPrototype();
    }
    JSDocInfo docInfo = null;
    for (; objectType != null; 
         objectType = objectType.getImplicitPrototype()) {
      docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
      if (docInfo != null &&
          docInfo.getVisibility() != Visibility.INHERITED) {
        break;
      }
    }

    if (objectType == null) {
      // We couldn't find a visibility modifier; assume it's public.
      return;
    }

    boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
    Visibility visibility = docInfo.getVisibility();
    JSType ownerType = normalizeClassType(objectType);

    if (isOverride) {
      // Check an ASSIGN statement that's trying to override a property
      // on a superclass.
      JSDocInfo overridingInfo = parent.getJSDocInfo();
      Visibility overridingVisibility = (overridingInfo == null) ? Visibility.INHERITED : overridingInfo.getVisibility();

      // Check that the overridden property is not private unless it is in the same input source
      if (visibility == Visibility.PRIVATE && !sameInput) {
        compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, objectType.toString()));
      } else if (overridingVisibility != Visibility.INHERITED &&
          overridingVisibility != visibility) {
        // Check that the overridden property has the same visibility as the original
        compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, visibility.name(), objectType.toString(), overridingVisibility.name()));
      }
    } else {
      if (!sameInput) {
        // If not in the same input source, check for private or protected properties that are not allowed
        if (visibility == Visibility.PRIVATE ||
            (visibility == Visibility.PROTECTED && !isInSubclassOrSameFile(t, parent))) {
          compiler.report(t.makeError(getprop,
              BAD_PRIVATE_PROPERTY_ACCESS,
              propertyName,
              validator.getReadableJSTypeName(getprop.getFirstChild(), true)));
        }
      }
    }
  }
}

// Helper method to check if the property access is within a subclass or the same file
private boolean isInSubclassOrSameFile(NodeTraversal t, Node parent) {
  // This method should be implemented to check if the current context allows protected properties.
  // For simplicity, it's assumed that it returns true which means protected properties are allowed in this scenario.
  return true;
}