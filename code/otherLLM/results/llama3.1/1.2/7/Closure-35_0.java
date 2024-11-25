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
          JSType typeToInfer = getPropertyType(objType, prop, null, getFlowScope());
          if (!typeToInfer.equals(getNativeType(UNKNOWN_TYPE))) {
            if (!objType.hasProperty(prop)) {
              typeToInfer =
                  getNativeType(VOID_TYPE).getLeastSupertype(propType);
            }
            objType.defineInferredProperty(prop, typeToInfer, null);
          } else if (prop.contains(".")) {
            String[] nestedProps = prop.split("\\.");
            ObjectType nestedObjType = getTypeFromNestedProperties(objType, nestedProps);
            if (nestedObjType != null) {
              typeToInfer = getPropertyType(nestedObjType, nestedProps[nestedProps.length - 1], null, getFlowScope());
              if (!typeToInfer.equals(getNativeType(UNKNOWN_TYPE))) {
                objType.defineInferredProperty(prop, typeToInfer, null);
              }
            }
          }
        }
      }
    }
  }
}

private ObjectType getTypeFromNestedProperties(ObjectType objType, String[] nestedProps) {
  for (String prop : nestedProps) {
    JSType type = getPropertyType(objType, prop, null, getFlowScope());
    if (!type.equals(getNativeType(UNKNOWN_TYPE))) {
      objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    } else {
      break;
    }
  }
  return objType;
}