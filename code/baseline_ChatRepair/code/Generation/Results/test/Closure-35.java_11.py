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
        JSType existingPropType = objType.getPropertyType(prop);

        // Proactively determine if the property type needs to be inferred or not
        // Check if the property is not declared and if the property doesn't have the correct type aligned with the constraint
        if (!objType.isPropertyTypeDeclared(prop) && 
            (existingPropType == null || !existingPropType.isEquivalentTo(propType))) {
          // Define or update the property type
          JSType typeToInfer;
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          } else {
            typeToInfer = existingPropType.getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}
