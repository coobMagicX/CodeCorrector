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
        
        // Check if property is declared in the object type
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            // Attempt to define the property with a default type
            boolean defined = ensurePropertyDeclaredHelper(constraintObj, objType);
            
            // If not already declared and could not be addressed by helper, infer type
            if (!defined) {
              if (prop.equals("prototype")) {
                // Handle prototype declarations
                // Assuming we have some logic to determine the correct type for 'prototype'
                typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
              } else {
                // If not a prototype, infer a least supertype with VOID
                typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
              }
            }
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}