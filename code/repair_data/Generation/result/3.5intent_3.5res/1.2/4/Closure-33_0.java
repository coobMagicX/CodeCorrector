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
        JSType typeToInfer = propType;
        if (!hasProperty(prop)) {
          if (constraintObj.hasProperty(prop)) {
            typeToInfer = constraintObj.getPropertyType(prop);
          } else if (constraintObj.hasProperty(prop + ".text")) {
            typeToInfer = constraintObj.getPropertyType(prop + ".text");
          } else {
            typeToInfer = getNativeType(JSTypeNative.OBJECT_TYPE);
          }
        }
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}