private void checkPropertyVisibility(NodeTraversal t, Node getprop, Node parent) {
  ObjectType objectType = ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();

  if (objectType != null) {
    ObjectType originalObjectType = objectType;  // References the initial objectType
    boolean isOverride = t.inGlobalScope() &&
      parent.getType() == Token.ASSIGN &&
      parent.getFirstChild() == getprop;

    JSDocInfo docInfo = null;
    while (objectType != null) {
      docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
      if (docInfo != null && docInfo.getVisibility() != Visibility.INHERITED) {
        break;
      }
      objectType = objectType.getImplicitPrototype();  // Move to the next prototype in the chain
    }

    if (objectType == null) {
      return;  // No additional checks needed if no property found
    }

    boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
    Visibility visibility = docInfo.getVisibility();
    JSType ownerType = normalizeClassType(objectType);

    if (isOverride) {
      checkOverrideVisibility(t, getprop, parent, visibility, sameInput, originalObjectType);
    } else {
      checkDirectUsageVisibility(t, getprop, parent, sameInput, visibility, ownerType, propertyName, docInfo);
    }
  }
}

private void checkOverrideVisibility(NodeTraversal t, Node getprop, Node parent, Visibility visibility, boolean sameInput, ObjectType objectType) {
  JSDocInfo overridingInfo = parent.getJSDocInfo();
  Visibility overridingVisibility = overridingInfo == null ? Visibility.INHERITED : overridingInfo.getVisibility();

  if ((visibility == Visibility.PRIVATE && !sameInput)) {
    compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, objectType.toString()));
  } else if (overridingVisibility != Visibility.INHERITED && overridingVisibility != visibility) {
    compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, visibility.name(), objectType.toString(), overridingVisibility.name()));
  }
}

private void checkDirectUsageVisibility(NodeTraversal t, Node getprop, Node parent, boolean sameInput, Visibility visibility, JSType ownerType, String propertyName, JSDocInfo docInfo) {
  if (sameInput) {
    return;
  } else if (visibility == Visibility.PRIVATE && (currentClass == null || ownerType.differsFrom(currentClass))) {
    if (docInfo.isConstructor() && isValidPrivateConstructorAccess(parent)) {
      return;
    }

    compiler.report(t.makeError(getprop, BAD_PRIVATE_PROPERTY_ACCESS, propertyName, validator.getReadableJSTypeName(getprop.getFirstChild(), true)));
  } else if (visibility == Visibility.PROTECTED) {
    if (currentClass == null || !currentClass.isSubtype(ownerType)) {
      compiler.report(t.makeError(getprop, BAD_PROTECTED_PROPERTY_ACCESS, propertyName, validator.getReadableJSTypeName(getprop.getFirstChild(), true)));
    }
  }
}
