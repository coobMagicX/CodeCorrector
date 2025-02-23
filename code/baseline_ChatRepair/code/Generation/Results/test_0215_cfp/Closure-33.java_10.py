public void matchConstraint(ObjectType constraintObj) {
  // Handle the case where the constraint object is a record type
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);

      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer;
        
        // Define the property directly with the inferred type if it doesn't exist
        if (!hasProperty(prop)) {
          typeToInfer = propType;
        } else {
          JSType existingType = getPropertyType(prop);
          typeToInfer = existingType.getLeastSupertype(propType);
          
          // If the type to infer is not equivalent to the existing type, log a warning
          if (!typeToInfer.isEquivalentTo(existingType)) {
            logWarning("Type mismatch for property '" + prop + "'. Expected: " + propType +
                       ", but was: " + existingType);
          }
        }

        // If there is still no type defined for the property, define it. 
        if (typeToInfer != null) {
          defineInferredProperty(prop, typeToInfer, null);
        } else {
          logWarning("Failed to infer non-existing property type for '" + prop + "'.");
        }
      }
    }
  }
}

// Example stub method for logging warning
private void logWarning(String message) {
  System.err.println(message); // You could change this line to integrate with your logging framework
}
