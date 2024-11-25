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
        
        // Check if the property is declared in the object type
        if (!objType.isPropertyTypeDeclared(prop)) {
          // If not, infer the type. If it's an optional property that doesn't exist in the object type,
          // get the least supertype from VoidType.
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          
          // Define the inferred property in the object type
          objType.defineInferredProperty(prop, typeToInfer, null);

          // Also ensure that the property is accounted for when checking against the constraint
          if (typeToInfer != propType) {
            // If the inferred type is different from the original type,
            // we need to update the object type to reflect this change.
            objType = ObjectType.cast(objType.restrictByAddingProperty(prop, typeToInfer));
          }
        }
      }
    }
  }
}