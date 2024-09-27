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
          if (propType.isUnionType()) {
            JSType leastSupertype = null;
            for (JSType type : propType.getUnionMembers()) {
              if (type.isSubtype(getNativeType(JSTypeNative.NULL_TYPE))) {
                continue;
              } else if (leastSupertype == null) {
                leastSupertype = type;
              } else {
                leastSupertype = leastSupertype.getLeastSupertype(type);
              }
            }
            if (leastSupertype == null) {
              typeToInfer = propType;
            } else {
              typeToInfer = leastSupertype;
            }
          } else {
            typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                .getLeastSupertype(propType);
          }
          defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}