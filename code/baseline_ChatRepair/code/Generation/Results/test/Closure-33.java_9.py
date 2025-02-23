public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      // Only handle properties that are not already declared on this object.
      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer = propType;
        // If the property does not exist on this object, we shouldn't define a VOID type.
        // Instead, just define it with the property type from the constraint.
        if (!hasProperty(prop)) {
          typeToInfer = propType;
        }
        // Define the property directly with the inferred type.
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}
