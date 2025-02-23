public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

  // Handle the case where the constraint object is a record type.
  //
  // param constraintObj {{prop: (number|undefined)}}
  // function f(constraintObj) {}
  // f({});
  //
  // We want to modify the object literal to match the constraint, by
  // taking each property on the record and trying to match
  // properties on this object.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      // Check if the current property is already declared.
      if (hasProperty(prop)) {
        JSType existingType = getPropertyType(prop);
        if (existingType == null || existingType.isEmptyType()) {
          defineInferredProperty(prop, propType, null);
        } else if (!existingType.isEquivalentTo(propType)) {
          defineInferredProperty(prop, existingType.getLeastSupertype(propType), null);
        }
      } else {
        // Case when this object does not have the property at all.
        // Infer the most general of void and provided type to help with type inference.
        JSType typeToInfer = propType;
        if (isPropertyTypeDeclared(prop)) {
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE).getLeastSupertype(propType);
        }
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}
