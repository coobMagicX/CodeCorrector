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
        
        // Check if the property is already declared with a type
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          
          // If the property doesn't exist in the object type, infer a suitable type
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          
          // Only define the inferred property if it's not already defined
          if (typeToInfer != null) {
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}