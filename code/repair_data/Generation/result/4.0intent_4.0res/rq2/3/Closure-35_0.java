private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj =
      ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        JSType typeToInfer = propType;
        // Check if the object type does not declare the property or it's in different branches
        if (!objType.isPropertyTypeDeclared(prop) || !objType.hasProperty(prop)) {
          if (!objType.hasProperty(prop)) {
            // If property does not exist, infer it as the least supertype of VOID_TYPE and the required type
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          } else if (objType.getPropertyType(prop) != null) {
            // Combine existing property type with the new type requirement
            typeToInfer = objType.getPropertyType(prop).getLeastSupertype(propType);
          }
          // Define or redefine the inferred property
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}