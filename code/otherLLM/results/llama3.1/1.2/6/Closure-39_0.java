String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint && !forAnnotations) { // Added condition to check forAnnotations
    // Don't pretty print recursively.
    prettyPrint = false;

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType() &&
           propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES) {
      propertyNames.addAll(current.getOwnPropertyNames());
      current = current.getImplicitPrototype();
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
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    prettyPrint = true;
    return sb.toString();
  } else { // Modified to handle forAnnotations
    StringBuilder sb = new StringBuilder();
    List<String> propertyNames = getSortedPropertyNames(true);
    for (String property : propertyNames) {
      sb.append(property).append(": ").append(getPropertyType(property).toString()).append(", ");
    }
    return sb.toString().substring(0, sb.length() - 2); // Truncate the last comma and space
  }
}