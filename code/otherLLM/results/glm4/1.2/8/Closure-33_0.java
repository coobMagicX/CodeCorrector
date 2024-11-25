public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.

    // Handle the case where the constraint object is a record type.
    if (constraintObj.isRecordType()) {
        for (String prop : constraintObj.getOwnPropertyNames()) {
            JSType propType = constraintObj.getPropertyType(prop);

            // Ensure that propertyType is not null
            if (propType != null) {
                // Check if the property type has been declared already
                if (!isPropertyTypeDeclared(prop)) {
                    JSType typeToInfer = propType;

                    // If the property doesn't exist, infer a type based on void and the property type
                    if (!hasProperty(prop)) {
                        typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                            .getLeastSupertype(propType);
                    }

                    // Define the inferred property with the correct parameters
                    defineInferredProperty(prop, typeToInfer, null);
                }
            } else {
                // If propType is null, handle it according to your error handling policy or requirements
                System.out.println("Warning: Property type for '" + prop + "' is null.");
            }
        }
    }
}