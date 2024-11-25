String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint && currentObject != null) {
    // Don't pretty print recursively.
    boolean originalPrettyPrint = prettyPrint;
    prettyPrint = false;

    StringBuilder sb = new StringBuilder();
    Set<String> propertyNames = Sets.newTreeSet();

    // Check for circular reference by tracking seen objects
    Set<ObjectType> seenObjects = new HashSet<>();
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType() &&
           (!seenObjects.contains(current) || propertyNames.size() < MAX_PRETTY_PRINTED_PROPERTIES)) {
      seenObjects.add(current);
      propertyNames.addAll(current.getOwnPropertyNames());
      current = current.getImplicitPrototype();
    }

    // Restore the original prettyPrint value
    prettyPrint = originalPrettyPrint;

    if (propertyNames.isEmpty()) {
      return "{...}";
    } else {
      sb.append("{");

      int i = 0;
      for (String property : propertyNames) {
        if (i > 0) {
          sb.append(", ");
        }

        sb.append(property);
        sb.append(": ");
        sb.append(getPropertyType(property).toString());

        ++i;
        if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
          sb.append(", ...");
          break;
        }
      }

      sb.append("}");

      return sb.toString();
    }
  } else {
    return "{...}";
  }
}