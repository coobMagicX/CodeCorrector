public void matchConstraint(ObjectType constraintObj) {
  // Only match constraints on anonymous types if the object is a record type.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      
      // Check if the current object does not declare this property,
      // or if the declared property allows undefined values which can accommodate additional types
      if (!this.hasOwnProperty(prop)) {
        // If property does not exist at all, the type to infer should accommodate `undefined`.
        if (propType.isUnionType()) { // Check if the constraint property type allows undefined.
          if (!propType.isVoidType()) { // Ensure 'undefined' or similar is not the sole type.
            JSType broaderType = getNativeType(JSTypeNative.ALL_TYPE); // Represents any type.
            defineInferredProperty(prop, broaderType, null);
          }
        } else {
          // Infer as it is if not a union type that contains undefined.
          defineInferredProperty(prop, propType, null);
        }
      }
    }
  }
}
