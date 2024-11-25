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
        JSType propType = getPropertyType(objType, prop, null, null);
        if (!objType.isPropertyTypeDeclared(prop)) {
          JSType typeToInfer = propType;
          if (!objType.hasProperty(prop)) {
            // Ensure we do not get a null LeastSupertype
            typeToInfer = (typeToInfer != null) ? getNativeType(VOID_TYPE).getLeastSupertype(typeToInfer) : getNativeType(UNKNOWN_TYPE);
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}

// Method to be used for obtaining property types.
private JSType getPropertyType(JSType objType, String propName,
    Node n, FlowScope scope) {
  // ... (method implementation remains unchanged)
}