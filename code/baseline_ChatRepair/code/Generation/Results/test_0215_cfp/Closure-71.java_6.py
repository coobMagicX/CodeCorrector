if (objectType == null) {
  // We couldn't find a visibility modifier; assume it's public unless overridden.
  return;
}

boolean sameInput = docInfo != null && t.getInput().getName().equals(docInfo.getSourceName());
Visibility visibility = docInfo == null ? Visibility.PUBLIC : docInfo.getVisibility();
JSType ownerType = normalizeClassType(objectType);

if (isOverride) {
  // Check an ASSIGN statement that's trying to override a property
  // on a superclass.
  JSDocInfo overridingInfo = parent.getJSDocInfo();
  Visibility overridingVisibility = overridingInfo == null ?
      Visibility.INHERITED : overridingInfo.getVisibility();

  if (visibility == Visibility.PRIVATE) {
    if (!sameInput) {
      compiler.report(
          t.makeError(getprop, PRIVATE_OVERRIDE_ERROR,
          propertyName, objectType.toString()));
    }
    return; // When private, it should not allow overriding from other inputs.
  } else if (overridingVisibility != Visibility.INHERITED &&
           overridingVisibility != visibility) {
    compiler.report(
        t.makeError(getprop, VISIBILITY_MISMATCH,
          visibility.name(), objectType.toString(),
          overridingVisibility.name()));
    }
} else {
  handleNonOverrideVisibility(t, sameInput, visibility, parent, getprop, objectType, ownerType, propertyName);
}
