public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

  // Handle the case where the constraint object is a record type.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      
      // Check if property type is declared, if not declared then infer
      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer = propType;
        
        // If property does not exist on this object, then infer the least supertype
        if (!hasProperty(prop)) {
          typeToInfer = getNativeType(JSTypeNative.UNKNOWN_TYPE).getLeastSupertype(propType);
        }
        
        // Define or infer the property type on this object
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}