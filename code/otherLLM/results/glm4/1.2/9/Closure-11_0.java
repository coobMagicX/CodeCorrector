private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  // Lots of types can appear on the left, a call to a void function can
  // never be on the left. getPropertyType will decide what is acceptable
  // and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  
  JSType childType = getJSType(objNode);

  if (childType == null) {
    report(t, n, TypeValidator.NULL_POINTER_ACCESS, "No properties on this expression", getNativeType(OBJECT_TYPE));
    return;
  }

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    // This check seems redundant as it's already covered by the 'else if' below.
    // If this is intended to handle an assignment that doesn't expect a property access,
    // please provide further context or details on what exactly should be done here.
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  
  ensureTyped(t, n);
}