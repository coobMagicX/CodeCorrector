private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        // Check if the object type already has the property with a declared or inferred type.
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType currentType = objType.getPropertyType(prop);
          // Decide on the type to infer only if there is no current accepted type.
          if (currentType == null || currentType.isUnknownType()) {
            // Define or infer the property type if not previously defined.
            JSType typeToInfer = propType;
            if (!objType.hasProperty(prop)) {
              typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
            }
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}
