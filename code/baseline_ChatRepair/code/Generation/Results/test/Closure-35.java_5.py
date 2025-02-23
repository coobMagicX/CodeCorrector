private void inferPropertyTypesToMatchConstraint(JSType type, JSType constraint) {
  if (type == null || constraint == null) {
    return;
  }

  ObjectType constraintObj = ObjectType.cast(constraint.restrictByNotNullOrUndefined());
  if (constraintObj != null && constraintObj.isRecordType()) {
    ObjectType objType = ObjectType.cast(type.restrictByNotNullOrUndefined());
    if (objType != null) {
      for (String prop : constraintObj.getOwnPropertyNames()) {
        JSType propType = constraintObj.getPropertyType(prop);
        // Only infer the property type if the object type does not have it declared
        if (!objType.isPropertyTypeDeclared(prop)) {
          // If the property exists on the object or it's undefined, set type; otherwise, leave it be
          if (objType.hasProperty(prop) || objType.getPropertyType(prop) == null) {
            objType.defineInferredProperty(prop, propType, null);
          }
        }
      }
    }
  }
}
