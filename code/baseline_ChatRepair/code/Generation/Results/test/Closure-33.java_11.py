public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.

    if (constraintObj.isRecordType()) {
        for (String prop : constraintObj.getOwnPropertyNames()) {
            JSType propType = constraintObj.getPropertyType(prop);

            // If the property is not declared and does not exist, define it with the given type.
            if (!hasProperty(prop) && !isPropertyTypeDeclared(prop)) {
                defineInferredProperty(prop, propType, null);
            } else if (isPropertyTypeDeclared(prop)) {
                // If property is declared, check if existing type and constraint type are compatible.
                JSType existingType = getPropertyType(prop);

                if (!existingType.isSubtype(propType)) { // ensure existing type matches constraint type expectation
                    // Log a warning when existing property type does not meet constraint type.
                    throw new AssertionError("Warning: Property '" + prop + "' type mismatch. Expected "
                        + propType + ", but found " + existingType);
                }
            } else {
                // If property is undeclared and exists, infer the minimum type to satisfy both.
                JSType existingType = getPropertyType(prop);
                JSType leastUpperBoundType = existingType.getLeastSupertype(propType);

                if (!leastUpperBoundType.isEquivalentTo(existingType)) {
                    // Update with a more generic type if needed
                    defineInferredProperty(prop, leastUpperBoundType, null);
                }

                // Generate a warning if the defined type does not cover both existing and inferred types.
                if (!leastUpperBoundType.isSubtypeOf(existingType) && !leastUpperBoundType.isSubtypeOf(propType)) {
                    throw new AssertionError("Warning: Property '" + prop + "' could not fully match expected type and existing type.");
                }
            }
        }
    }
}
