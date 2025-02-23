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
        // Check if the property is declared and if not, proceed with inferring it.
        if (!objType.isPropertyTypeDeclared(prop)) {
          // Only infer the property type if it's not already present on the object type.
          if (!objType.hasProperty(prop)) {
            JSType typeToInfer = propType;
            // Use the least supertype of existing property type and the constrained property type
            // if the property already exists in the object to ensure consistency.
            if (objType.hasProperty(prop)) {
              typeToInfer = objType.getPropertyType(prop).getLeastSupertype(propType);
            }
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}
