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
      Map<String, JSType> propTypes = new HashMap<>();
      
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        propTypes.put(prop, propType);
        
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          
          if (!objType.hasProperty(prop)) {
            typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }

      // Handle undefined properties in the object type
      for (String prop : objType.getOwnPropertyNames()) {
        if (!propTypes.containsKey(prop)) {
          JSType typeToInfer = getNativeType(VOID_TYPE);
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}