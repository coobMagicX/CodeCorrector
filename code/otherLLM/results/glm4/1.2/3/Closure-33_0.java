public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

  // Handle the case where the constraint object is a record type.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      
      // Check if the property has already been declared
      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer;
        
        // If the property does not exist on this object, infer a default type
        if (!hasProperty(prop)) {
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE).getLeastSupertype(propType);
        } else {
          // Otherwise, use the type provided by the constraint object
          typeToInfer = propType;
        }
        
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}