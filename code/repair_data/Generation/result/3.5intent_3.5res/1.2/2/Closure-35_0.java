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
      Set<String> allProps = new HashSet<>(constraintObj.getOwnPropertyNames());
      allProps.addAll(objType.getOwnPropertyNames());

      for (String prop : allProps) {
        JSType propType = constraintObj.getPropertyType(prop);
        if (objType.hasProperty(prop)) {
          JSType existingType = objType.getPropertyType(prop);
          if (existingType != null) {
            propType = existingType.getLeastSupertype(propType);
          }
        }

        objType.defineInferredProperty(prop, propType, null);
      }
    }
  }
}