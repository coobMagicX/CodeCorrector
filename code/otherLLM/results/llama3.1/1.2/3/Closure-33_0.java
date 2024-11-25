public void matchConstraint(ObjectType constraintObj) {
  // We only want to match contraints on anonymous types.

  // Handle the case where the constraint object is a record type.
  //
  // param constraintObj {{prop: (number|undefined)}}
  // function f(constraintObj) {}
  // f({});
  //
  // We want to modify the object literal to match the constraint, by
  // taking any each property on the record and trying to match
  // properties on this object.
  if (constraintObj.isRecordType()) {
    for (String prop : constraintObj.getOwnPropertyNames()) {
      JSType propType = constraintObj.getPropertyType(prop);
      if (!isPropertyTypeDeclared(prop)) {
        if (propType.getTypes().size() > 1) { // Check if there are multiple possible types
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
              .getLeastSupertype(propType);
        } else {
          typeToInfer = propType;
        }
      } else {
        typeToInfer = propType;
      }
      defineInferredProperty(prop, typeToInfer, null);
    }
  }
}