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
      Set<String> allProps = new HashSet<>();
      allProps.addAll(constraintObj.getOwnPropertyNames());
      
      // Initialize a map to hold property types inferred from each branch
      Map<String, JSType> inferredTypes = new HashMap<>();
      
      // Iterate through constraint object properties
      for (String prop : allProps) {
        JSType propType = constraintObj.getPropertyType(prop);
        JSType typeToInfer = propType;
        
        if (!objType.isPropertyTypeDeclared(prop)) {
          for (JSType branchType : new JSType[] {objType, getNativeType(VOID_TYPE)}) {
            if (branchType.hasProperty(prop)) {
              JSType existingType = inferredTypes.get(prop);
              if (existingType == null) {
                inferredTypes.put(prop, branchType.getPropertyType(prop));
              } else {
                JSType combinedType = existingType.getLeastSupertype(branchType.getPropertyType(prop));
                inferredTypes.put(prop, combinedType);
              }
            }
          }
        }
      }
      
      // Define inferred properties with the most common type
      for (Map.Entry<String, JSType> entry : inferredTypes.entrySet()) {
        objType.defineInferredProperty(entry.getKey(), entry.getValue(), null);
      }
    }
  }
}