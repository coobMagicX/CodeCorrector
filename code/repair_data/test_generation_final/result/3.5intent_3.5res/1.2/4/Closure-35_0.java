private void inferPropertyTypesToMatchConstraint(
    JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj =
      ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            typeToInfer =
                getNativeType(VOID_TYPE).getLeastSupertype(propType);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }

    // Iterate through each branch of a conditional statement
    for (Node branch : constraintObj.getConditionalBranches()) {
      objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
      if (objType != null) {
        for (String prop : branch.getOwnPropertyNames()) {
          JSType propType = branch.getPropertyType(prop);
          if (!objType.isPropertyTypeDeclared(prop)) {
            JSType typeToInfer = propType;
            if (!objType.hasProperty(prop)) {
              typeToInfer =
                  getNativeType(VOID_TYPE).getLeastSupertype(propType);
            }
            objType.defineInferredProperty(prop, typeToInfer, null);
          }
        }
      }
    }
  }
}