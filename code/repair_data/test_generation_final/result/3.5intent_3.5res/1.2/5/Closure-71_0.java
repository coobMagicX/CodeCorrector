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

    boolean sameInput =
        t.getInput().getName().equals(docInfo.getSourceName());
    Visibility visibility = docInfo.getVisibility();
    JSType ownerType = normalizeClassType(objectType);
    if (isOverride) {
      // Check an ASSIGN statement that's trying to override a property
      // on a superclass.
      JSDocInfo overridingInfo = parent.getJSDocInfo();
      Visibility overridingVisibility = overridingInfo == null ?
          Visibility.INHERITED : overridingInfo.getVisibility();

      // Check that (a) the property *can* be overridden, and
      // (b) that the visibility of the override is the same as the
      // visibility of the original property.
      if (visibility == Visibility.PRIVATE && !sameInput) {
        compiler.report(
            t.makeError(getprop, PRIVATE_OVERRIDE,
                objectType.toString()));
      } else if (overridingVisibility != Visibility.INHERITED &&
          overridingVisibility != visibility) {
        compiler.report(
            t.makeError(getprop, VISIBILITY_MISMATCH,
                visibility.name(), objectType.toString(),
                overridingVisibility.name()));
      } else if (overridingVisibility != Visibility.PRIVATE && !sameInput && !docInfo.isPrivate()) {
        compiler.report(
            t.makeError(getprop, VISIBILITY_MISMATCH,
                "private", objectType.toString(),
                overridingVisibility.name()));
      }
    } else {
      if (sameInput) {
        // private access is always allowed in the same file.
        return;
      } else if (visibility == Visibility.PRIVATE &&
          (currentClass == null || ownerType.differsFrom(currentClass))) {
        if (docInfo.isConstructor() &&
            isValidPrivateConstructorAccess(parent)) {
          return;
        }

        // private access is not allowed outside the file from a different
        // enclosing class.
        compiler.report(
            t.makeError(getprop,
                BAD_PRIVATE_PROPERTY_ACCESS,
                propertyName,
                validator.getReadableJSTypeName(
                    getprop.getFirstChild(), true)));
      } else if (visibility == Visibility.PROTECTED) {
        // There are 3 types of legal accesses of a protected property:
        // 1) Accesses in the same file
        // 2) Overriding the property in a subclass
        // 3) Accessing the property from inside a subclass
        // The first two have already been checked for.
        if (currentClass == null || !currentClass.isSubtype(ownerType)) {
          compiler.report(
              t.makeError(getprop,  BAD_PROTECTED_PROPERTY_ACCESS,
                  propertyName,
                  validator.getReadableJSTypeName(
                      getprop.getFirstChild(), true)));
        }
      }
    }
  }
}