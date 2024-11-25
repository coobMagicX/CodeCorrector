public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.

    // Handle the case where the constraint object is a record type.
    if (constraintObj.isRecordType()) {
        Set<String> propertyNames = new HashSet<>();
        collectPropertyNames(propertyNames);

        for (String prop : propertyNames) {
            JSType propType = constraintObj.getPropertyType(prop);
            if (!isPropertyTypeDeclared(prop)) {
                JSType typeToInfer = propType;
                // If the property does not exist on the object, infer a default type.
                if (!hasProperty(prop)) {
                    typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                            .getLeastSupertype(propType);
                }
                defineInferredProperty(prop, typeToInfer, null);
            }
        }
    }
}