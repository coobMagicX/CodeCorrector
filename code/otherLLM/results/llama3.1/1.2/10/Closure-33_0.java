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
      
      // Check if the property is a nested property access
      if (prop.contains(".")) {
        String[] nestedProps = prop.split("\\.");
        JSType typeToInfer = getNestedPropertyType(nestedProps[0], nestedProps[1]);
        
        // If the property is not declared, infer its type
        if (!isPropertyTypeDeclared(prop)) {
          defineInferredProperty(prop, typeToInfer, null);
        }
      } else {
        JSType typeToInfer = propType;
        
        // If the property is not declared, infer its type
        if (!isPropertyTypeDeclared(prop)) {
          if (hasProperty(prop)) {
            typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                .getLeastSupertype(propType);
          }
          defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}