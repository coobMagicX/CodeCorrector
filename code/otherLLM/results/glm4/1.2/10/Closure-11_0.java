private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  //Lots of types can appear on the left, a call to a void function can
  //never be on the left. getPropertyType will decide what is acceptable
  //and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType == null) {
    // If the type of objNode is null, it means it's a 'null' object.
    report(t, objNode, TypeValidator.NON_NULLASSIGNMENT, "Attempted to set property on a 'null' object", getNativeType(OBJECT_TYPE));
  } else if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    return; // This handles cases where an assignment is attempted on a non-null object.
  } else if (!validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n); // Ensures the node is typed correctly.
}