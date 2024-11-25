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
      
      // Check if the property type is declared, if not we need to infer it.
      if (!isPropertyTypeDeclared(prop)) {
        JSType typeToInfer;
        
        // If the property does not exist on this object, we need to infer a suitable type.
        if (!hasProperty(prop)) {
          typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
              .getLeastSupertype(propType);
        } else {
          // If the property is already declared, use its type.
          typeToInfer = propType;
        }

        // Define the inferred property on this object.
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}