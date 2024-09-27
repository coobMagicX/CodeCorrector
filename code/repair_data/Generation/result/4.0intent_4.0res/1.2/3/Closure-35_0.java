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
        JSType existingPropType = objType.getPropertyType(prop);
        JSType typeToInfer;
        if (existingPropType == null) {
          // If the property does not exist on the object, infer it as VOID_TYPE or the property type
          typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
        } else if (!objType.isPropertyTypeDeclared(prop)) {
          // If the property type is not declared, merge the existing type with the constraint type
          typeToInfer = existingPropType.getLeastSupertype(propType);
        } else {
          // Use the declared property type if it exists
          typeToInfer = existingPropType;
        }
        objType.defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}