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
        JSType typeToInfer;
        if (hasProperty(prop)) {
          // If the property already exists, try to infer its type
          // by getting the least supertype of the existing type and the
          // property type in the constraint object.
          JSType existingType = getObjectType().getPropertyType(prop);
          typeToInfer = existingType.getLeastSupertype(propType);
        } else {
          // If the property does not exist, try to infer its type by taking
          // the least supertype of the void type and the property type in the
          // constraint object.
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
              .getLeastSupertype(propType);
        }
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}