public void matchConstraint(ObjectType constraintObj) {
    // We only want to match constraints on anonymous types.

    // Handle the case where the constraint object is a record type.
    if (constraintObj.isRecordType()) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = getPropertyType(prop);
        if (!isPropertyTypeDeclared(prop)) {
          JSType typeToInfer;
          // If property does not exist, infer the type as void or the least supertype.
          if (!hasOwnProperty(prop)) {
            typeToInfer = getNativeType(JSTypeNative.VOID_TYPE).getLeastSupertype(propType);
          } else {
            typeToInfer = propType; // Property exists, use its type.
          }
          defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
}