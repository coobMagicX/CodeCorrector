public void matchConstraint(ObjectType constraintObj) {
  // We only want to match constraints on anonymous types.

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
          if (prop.indexOf('.') != -1) {
            String[] nestedProps = prop.split("\\.");
            ObjectType currentObj = constraintObj;
            for (int i = 0; i < nestedProps.length - 1; i++) {
              JSType nestedPropType = currentObj.getPropertyType(nestedProps[i]);
              if (!isPropertyTypeDeclared(nestedProps[i])) {
                JSType nestedTypeToInfer = nestedPropType;
                if (!currentObj.hasProperty(nestedProps[i])) {
                  nestedTypeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                      .getLeastSupertype(nestedPropType);
                }
                currentObj.defineInferredProperty(nestedProps[i], nestedTypeToInfer, null);
              }
              if (nestedPropType.isObjectType()) {
                currentObj = nestedPropType.toObjectType();
              } else {
                break;
              }
            }
            prop = nestedProps[nestedProps.length - 1];
            propType = currentObj.getPropertyType(prop);
          } else {
            typeToInfer = getNativeType(JSTypeNative.VOID_TYPE)
                .getLeastSupertype(propType);
          }
        }
        defineInferredProperty(prop, typeToInfer, null);
      }
    }
  }
}