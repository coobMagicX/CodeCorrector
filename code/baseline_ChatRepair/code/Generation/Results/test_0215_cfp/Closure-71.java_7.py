private void checkPropertyVisibility(NodeTraversal t,
                                     Node getprop, Node parent) {
  ObjectType objectType = ObjectType.cast(dereference(getprop.getFirstChild().getJSType()));
  String propertyName = getprop.getLastChild().getString();
  
  if (objectType != null) {
    boolean isOverride = t.inGlobalScope() &&
        parent.getType() == Token.ASSIGN &&
        parent.getFirstChild() == getprop;

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
 
    if (docInfo == null || objectType == null) {
      return;  // No visibility constraints found or not documented, hence public by default.
    }

    boolean sameInput = t.getInput() != null &&
                        docInfo.getSourceName() != null &&
                        docInfo.getSourceName().equals(t.getInput().getName());
    Visibility visibility = docInfo.getVisibility();
    
    if (isOverride) {
      // Check an ASSIGN statement that's trying to override a property
      JSDocInfo overridingInfo = parent.getJSDocInfo();
      Visibility overridingVisibility = overridingInfo == null ?
          Visibility.INHERITED : overridingInfo.getVisibility();

      if (visibility == Visibility.PRIVATE && !sameInput) {
        compiler.report(t.makeError(getprop, "PRIVATE_OVERRIDE",
                                   propertyName, objectType.toString()));
      } else if (overridingVisibility != Visibility.INHERITED &&
                 overridingVisibility != visibility) {
        compiler.report(t.makeError(getprop, "VISIBILITY_MISMATCH",
                                   visibility.name(), objectType.toString(),
                                   overridingVisibility.name()));
      }
    } else {
      checkNonOverrideVisibility(sameInput, docInfo, visibility, getprop, parent, propertyName, objectType);
    }
  }
}

private void checkNonOverrideVisibility(boolean sameInput, JSDocInfo docInfo,
                                        Visibility visibility, Node getprop, Node parent,
                                        String propertyName, ObjectType objectType) {
  // Implement checks for non-override cases if required
  // Similar to previous logic before the original incorrect fix was proposed.
}
