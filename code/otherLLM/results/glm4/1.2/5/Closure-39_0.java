import com.google.common.collect.Sets;

String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    boolean originalPrettyPrint = prettyPrint;
    prettyPrint = false;

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    for (ObjectType current = this;
         current != null && !current.isNativeObjectType() &&
             propertyNames.size() < MAX_PRETTY_PRINTED_PROPERTIES;
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

      sb.append(property);
      sb.append(": ");
      sb.append(getPropertyType(property).toString());

      ++i;
      // Check if the number of properties is equal to MAX_PRETTY_PRINTED_PROPERTIES before breaking
      if (i == MAX_PRETTY_PRINTED_PROPERTIES - 1) {
        sb.append(", ...");
        break; // Break out of the loop after adding MAX_PRETTY_PRINTED_PROPERTIES - 1 properties
      }
    }

    sb.append("}");

    // Restore original prettyPrint value after building the string representation
    prettyPrint = originalPrettyPrint;
    return sb.toString();
  } else {
    return "{...}";
  }
}