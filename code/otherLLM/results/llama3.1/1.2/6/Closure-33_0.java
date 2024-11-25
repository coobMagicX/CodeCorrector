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
        JSType typeToInfer = inferPropertyTypeWithinNestedFunctions(propType);
        if (!hasProperty(prop)) {
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
              .getLeastSupertype(propType);
        }
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}

// Modified method to infer property types within nested functions
private JSType inferPropertyTypeWithinNestedFunctions(JSType propType) {
  // Check if the property type is a function with inferred properties
  if (propType.isFunction() && hasInferredProperties(propType)) {
    // Recursively call this method on each inferred property
    for (JSType inferredProp : getInferredProperties(propType)) {
      JSType newPropType = inferPropertyTypeWithinNestedFunctions(inferredProp);
      propType = propType.getLeastSupertype(newPropType);
    }
  }
  return propType;
}