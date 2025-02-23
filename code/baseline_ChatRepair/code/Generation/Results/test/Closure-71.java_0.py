private void checkPropertyVisibility(NodeTraversal t,
    Node getprop, Node parent) {
  ObjectType objectType =
      ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();

  if (objectType != null) {
    boolean isOverride = t.inGlobalScope() &&
        parent.getType() == Token.ASSIGN &&
        parent.getFirstChild() == getprop;

    // Traverse upward to the highest definition of the property that has JSDoc info with visibility.
    JSDocInfo docInfo = null;
    for (; objectType != null;
         objectType = objectType.getImplicitPrototype()) {
      if (objectType.hasOwnProperty(propertyName)) {
        docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
        if (docInfo != null && docInfo.getVisibility() != Visibility.INHERITED) {
          break;
        }
      }
    }

    // If no doc info found, assume public.
    if (docInfo == null) {
      return;
    }

    boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
    Visibility visibility = docInfo.getVisibility();
    JSType ownerType = normalizeClassType(objectType);

    if (isOverride) {
      JSDocInfo overridingInfo = parent.getJSDocInfo();
      Visibility overridingVisibility = overridingInfo == null ?
          Visibility.INHERITED : overridingInfo.getVisibility();

      if (visibility == Visibility.PRIVATE) {
        if (!sameInput) {
          compiler.report(
              t.makeError(getprop, PRIVATE_OVERRIDE,
                  objectType.toString()));
        } else if (overridingVisibility != Visibility.INHERITED &&
               overridingVisibility != visibility) {
          compiler.report(
              t.makeError(getprop, VISIBILITY_MISMATCH,
                  visibility.name(), objectType.toString(),
                  overridingVisibility.name()));
        }
      }
    } else {
      checkNonOverrideAccess(t, getprop, parent, visibility, sameInput, propertyName, ownerType, objectType, docInfo);
    }
  }
}

private void checkNonOverrideAccess(NodeTraversal t, Node getprop, Node parent,
                                    Visibility visibility, boolean sameInput,
                                    String propertyName, JSType ownerType,
                                    ObjectType objectType, JSDocInfo docInfo) {
  // Additional handling for non-override cases.
  if (sameInput) {
    return;
  }

  if (visibility == Visibility.PRIVATE &&
      (currentClass == null || ownerType != null && ownerType.differsFrom(currentClass))) {
    compiler.report(
        t.makeError(getprop,
            BAD_PRIVATE_PROPERTY_ACCESS,
            propertyName,
            validator.getReadableJSTypeName(
                getprop.getFirstChild(), true)));
  } else if (visibility == Visibility.PROTECTED) {
    if (currentClass == null || (ownerType != null && !currentClass.isSubtype(ownerType))) {
      compiler.report(
          t.makeError(getprop, BAD_PROTECTED_PROPERTY_ACCESS,
              propertyName,
              validator.getReadableJSTypeName(
                  getprop.getFirstChild(), true)));
    }
  }
}
