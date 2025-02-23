private void checkPropertyVisibility(NodeTraversal t, Node getprop, Node parent) {
  ObjectType objectType = ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();

  if (objectType != null) {
    // Determine if the operation is an attempt to override a property
    boolean isOverride = t.inGlobalScope() && parent.getType() == Token.ASSIGN && parent.getFirstChild() == getprop;
    JSDocInfo docInfo = null;

    // Follow prototype chain looking for the property definition with documentation
    ObjectType definingObjectType = null;  // Keep track of where the property is actually defined
    for (ObjectType currentType = objectType; currentType != null; currentType = currentType.getImplicitPrototype()) {
      docInfo = currentType.getOwnPropertyJSDocInfo(propertyName);
      
      if (docInfo != null) {
        definingObjectType = currentType;  // Property has documentation here
        if (docInfo.getVisibility() != Visibility.INHERITED) {
          break;
        }
      }
    }

    // If no docInfo found in the prototype chain or visibility not tagged, assume public
    if (docInfo == null) return;

    Visibility propertyVisibility = docInfo.getVisibility();
    boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());

    if (isOverride) {
      // Additional checks for the case of an override attempt.
      handlePropertyOverride(t, getprop, parent, propertyVisibility, sameInput, definingObjectType);
    } else {
      // Check non-override access of property.
      handlePropertyAccess(t, getprop, propertyVisibility, sameInput, definingObjectType, propertyName);
    }
  }
}

private void handlePropertyOverride(NodeTraversal t, Node getprop, Node parent, Visibility visibility, boolean sameInput, ObjectType definingType) {
  JSDocInfo overridingInfo = parent.getJSDocInfo();
  Visibility overridingVisibility = overridingInfo == null ? Visibility.INHERITED : overridingInfo.getVisibility();

  if (visibility == Visibility.PRIVATE) {
    // Unauthorized override of a private property
    if (!sameInput) {
      compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, definingType.toString()));
    } else if (overridingVisibility != Visibility.PRIVATE && overridingVisibility != Visibility.INHERITED) {
      // Visibility mismatch case
      compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, visibility.name(), definingType.toString(), overridingVisibility.name()));
    }
  }
}

private void handlePropertyAccess(NodeTraversal t, Node getprop, Visibility visibility, boolean sameInput, ObjectType definingType, String propertyName) {
  if (sameInput || visibility == Visibility.PUBLIC) {
    // Same input file or public visibility requires no further checks
    return;
  }

  if (visibility == Visibility.PRIVATE) {
    // Unauthorized access to a private property
    compiler.report(t.makeError(getprop, BAD_PRIVATE_PROPERTY_ACCESS, propertyName, definingType.toString()));
  } else if (visibility == Visibility.PROTECTED) {
    if (currentClass == null || !currentClass.isSubtype(definingType)) {
      // Illegal access to a protected property from non-subtype
      compiler.report(t.makeError(getprop, BAD_PROTECTED_PROPERTY_ACCESS, propertyName, definingType.toString()));
    }
  }
}
