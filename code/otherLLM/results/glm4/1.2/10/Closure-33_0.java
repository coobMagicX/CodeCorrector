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
        // Check if the property is not present, and infer a type accordingly
        if (!hasProperty(prop)) {
          // If the property does not exist on this object, we need to infer the type.
          JSType nativeType = getNativeType(JSTypeNative.VOID_TYPE);
          JSType leastSupertype = nativeType.getLeastSupertype(propType);
          propType = leastSupertype; // Use the inferred type
        }
        defineInferredProperty(prop, propType, null); // Define with the inferred type
      }
    }
  }
}