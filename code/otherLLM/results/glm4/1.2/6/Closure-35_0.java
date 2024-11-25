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
            // Check if the property is optional by ensuring it's not declared in objType.
            // If the property is optional and not present, infer type from VOID_TYPE.
            if (prop.equals("prototype") || 
                (!objType.hasOwnProperty(prop) && 
                 (!objType.isInstanceType() || 
                  (ensurePropertyDeclaredHelper(new Node(Token.NAME), new ObjectType()).equals(false)))) {
              typeToInfer = getNativeType(VOID_TYPE).getLeastSupertype(propType);
            }
          }
          objType.defineInferredProperty(prop, typeToInfer, null);
        }
      }
    }
  }
}