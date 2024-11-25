public String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  }

  StringBuilder sb = new StringBuilder();
  Set<String> propertyNames = Sets.newTreeSet();
  Map<String, Boolean> seenObjects = new HashMap<>(); // New map to keep track of seen objects

  // Starting with the current object and all its properties, including prototypes
  ObjectType current = this;
  while (current != null && !current.isNativeObjectType()) {
    propertyNames.addAll(current.getOwnPropertyNames());
    seenObjects.put(current.getObjectId(), true); // Mark the current object as seen by its unique ID

    // Check if we've already processed the prototype to avoid infinite recursion
    ObjectType implicitPrototype = getImplicitPrototype();
    if (implicitPrototype != null && seenObjects.containsKey(implicitPrototype.getObjectId())) {
      break;
    }
    current = implicitPrototype;
  }

  sb.append("{");

  int i = 0;
  for (String property : propertyNames) {
    if (i > 0) {
      sb.append(", ");
    }

    // Check if the property has already been seen to prevent infinite recursion
    if (!seenObjects.containsKey(property)) {
      seenObjects.put(property, true); // Mark the property as seen

      sb.append(property);
      sb.append(": ");

      if (forAnnotations) {
        sb.append(getAnnotationString(property));
      } else {
        sb.append(getPropertyType(property).toString());
      }

      ++i;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }
  }

  sb.append("}");

  return sb.toString();
}

private String getAnnotationString(String propertyName) {
  // Placeholder for the actual implementation of getting annotations as a string
  return "";
}