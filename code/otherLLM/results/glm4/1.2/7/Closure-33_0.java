public void matchConstraint(ObjectType constraintObj) {
  // We only want to match contraints on anonymous types.

  // Handle the case where the constraint object is a record type.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      
      // Check if property is declared
      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer = propType;
        
        // If the property does not exist, infer a type considering undefined properties
        if (!hasProperty(prop)) {
          // Get the least supertype of void and the property's type
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
              .getLeastSupertype(propType);
        }
        
        // Define the inferred property
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}