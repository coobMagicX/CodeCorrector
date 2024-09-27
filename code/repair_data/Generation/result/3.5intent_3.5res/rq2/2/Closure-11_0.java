private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  // Lots of types can appear on the left, a call to a void function can
  // never be on the left. getPropertyType will decide what is acceptable
  // and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n);
}

private void checkPropertyAccess(JSType childType, String propName,
    NodeTraversal t, Node n) {
  // If the property type is unknown, check the object type to see if it
  // can ever be defined. We explicitly exclude CHECKED_UNKNOWN (for
  // properties where we've checked that it exists, or for properties on
  // objects that aren't in this binary).
  JSType propType = getJSType(n);
  if (propType == null || propType.isEquivalentTo(typeRegistry.getNativeType(UNKNOWN_TYPE))) {
    childType = childType.autobox();
    ObjectType objectType = ObjectType.cast(childType);
    if (objectType != null) {
      // We special-case object types so that checks on enums can be
      // much stricter, and so that we can use hasProperty (which is much
      // faster in most cases).
      if (!objectType.hasProperty(propName) ||
          objectType.isEquivalentTo(typeRegistry.getNativeType(UNKNOWN_TYPE))) {
        if (objectType instanceof EnumType) {
          report(t, n, INEXISTENT_ENUM_ELEMENT, propName);
        } else {
          checkPropertyAccessHelper(objectType, propName, t, n);
        }
      }

    } else {
      checkPropertyAccessHelper(childType, propName, t, n);
    }
  }
}