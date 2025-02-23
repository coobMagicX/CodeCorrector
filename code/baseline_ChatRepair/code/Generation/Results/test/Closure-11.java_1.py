private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  // Check for dictionary types and report illegal access
  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } 
  
  // Additional checks for null or undefined object types
  if (childType.isNullType() || childType.isVoidType()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "null or undefined");
  } 
  
  // Early return for assignments but check types if property access needs specific handling
  if (n.getJSType() != null && parent.isAssign()) {
    ensureTyped(t, n);
    return;
  }

  // Ensure the property access does not occur on null or undefined expressions and validate access
  if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }

  // Maintain the typing information across AST
  ensureTyped(t, n);
}
