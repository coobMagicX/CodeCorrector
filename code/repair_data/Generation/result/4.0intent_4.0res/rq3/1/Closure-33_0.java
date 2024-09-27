public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.

    // Handle the case where the constraint object is a record type.
    if (constraintObj.isRecordType()) {
        for (String prop : constraintObj.getOwnPropertyNames()) {
            JSType propType = constraintObj.getPropertyType(prop);
            if (!isPropertyTypeDeclared(prop)) {
                JSType typeToInfer = propType;
                if (!hasProperty(prop)) {
                    typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                        .getLeastSupertype(propType);
                }
                defineInferredProperty(prop, typeToInfer, null);
            }
        }
    }
}