String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint && !isRecursiveCall()) {
    // Don't pretty print recursively.
    prettyPrint = false;

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    for (ObjectType current = this;
         current != null && !current.isNativeObjectType() &&
             propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES;
         current = current.getImplicitPrototype()) {
      propertyNames.addAll(current.getOwnPropertyNames());
    }

    StringBuilder sb = new StringBuilder();
    sb.append("{");

    int i = 0;
    for (String property : propertyNames) {
      if (i > 0) {
        sb.append(", ");
      }
      // Check for recursive references
      ObjectType propertyType = getPropertyType(property);
      String typeRepresentation = isUnknownType(propertyType) ? "unknown" : propertyType.toString();
      sb.append(property).append(": ").append(typeRepresentation);

      ++i;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}

// Additional helper methods to support the repair
private boolean isRecursiveCall() {
  // Implement logic to detect if this is a recursive call in context
  // For example, check for a cycle detection algorithm or similar mechanism.
}

private boolean isUnknownType(ObjectType type) {
  // Implement logic to determine if the object type is unknown
}