private void checkPropertyVisibility(NodeTraversal t,
    Node getprop, Node parent) {
  ObjectType objectType =ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();

  if (objectType != null) {
    boolean isOverride = t.inGlobalScope() &&
        parent.getType() == Token.ASSIGN &&
        parent.getFirstChild() == getprop;

    JSDocInfo docInfo = null;
    if (isOverride) {
      objectType = objectType.getImplicitPrototype();
    }

    while (objectType != null) {
      docInfo = objectType.getOwnPropertyJSDocInfo(propertyName);
      if (docInfo != null && docInfo.getVisibility() != Visibility.INHERITED) {
        break;
      }
      objectType = objectType.getImplicitPrototype();
    }

    if (docInfo == null) {
      // We couldn't find a visibility modifier; assume it's public.
      return;
    }

    boolean sameInput = t.getInput().getName().equals(docInfo.getSourceName());
    Visibility visibility = docInfo.getVisibility();
    JSType ownerType = normalizeClassType(objectType);

    if (isOverride) {
      JSDocInfo overridingInfo = parent.getJSDocInfo();
      Visibility overridingVisibility = (overridingInfo != null) ? overridingInfo.getVisibility() : Visibility.INHERITED;

      if (visibility == Visibility.PRIVATE && !sameInput) {
        compiler.report(t.makeError(getprop, PRIVATE_OVERRIDE, objectType.toString()));
      } else if (overridingVisibility != Visibility.INHERITED && overridingVisibility != visibility) {
        compiler.report(t.makeError(getprop, VISIBILITY_MISMATCH, visibility.name(), objectType.toString(), overridingVisibility.name()));
      }
    } else {
      if (sameInput) {
        // private access is always allowed in the same file.
        return;
      } else if (visibility == Visibility.PRIVATE &&
          (currentClass == null || ownerType.differsFrom(currentClass))) {
        if (docInfo.isConstructor() && isValidPrivateConstructorAccess(parent)) {
          return;
        }

        // private access is not allowed outside the file from a different
        // enclosing class.
        compiler.report(t.makeError(getprop, BAD_PRIVATE_PROPERTY_ACCESS, propertyName,
            validator.getReadableJSTypeName(getprop.getFirstChild(), true)));
      } else if (visibility == Visibility.PROTECTED &&
          (currentClass == null || !currentClass.isSubtype(ownerType))) {
        compiler.report(t.makeError(getprop, BAD_PROTECTED_PROPERTY_ACCESS, propertyName,
            validator.getReadableJSTypeName(getprop.getFirstChild(), true)));
      }
    }
  }
}