private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          // Check if the property does not exist in the object type
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          // Define the inferred property only if it was previously undeclared
          if (typeToInfer != propType) { // Only define if there is a change in type
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}