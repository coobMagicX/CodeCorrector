public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);

      if (this.hasProperty(prop)) {
        JSType existingType = getPropertyType(prop);
        // Change warning conditions: only issue a warning if existing type can't be assigned to propType
        if (!existingType.isSubtype(propType)) {
          // Assume that reportMismatchError is a method to log or throw an error due to type mismatch
          reportMismatchError(prop, existingType, propType);
        }
      } else {
        // If property is not present on the object, this is typically unexpected since the object
        // is supposed to match the record type constraint.
        if (!isPropertyTypeDeclared(prop)) {
          defineInferredProperty(prop, propType, null);
        }
        reportMissingPropertyError(prop);  // Assuming this method doesn't exist, you may need to implement it.
      }
    }
  }
}

private void reportMismatchError(String property, JSType currentType, JSType expectedType) {
  throw new RuntimeException("Type mismatch for property '" + property + "': Expected " + expectedType + ", but got " + currentType);
}

private void reportMissingPropertyError(String property) {
  throw new RuntimeException("Property '" + property + "' is missing and should be defined.");
}
