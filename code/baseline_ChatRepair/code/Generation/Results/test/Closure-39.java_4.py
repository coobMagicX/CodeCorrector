String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    prettyPrint = false; // Turn off pretty printing to avoid infinite recursion

    // Use a tree set with explicit type parameters to ensure compatibility with Java 6.
    Set<String> propertyNames = new TreeSet<String>();
    for (ObjectType current = this;
         current != null && !current.isNativeObjectType() && propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES;
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
      JSType propertyType = getPropertyType(property);
      // Avoid recursive pretty print by not calling toString recursively if it is the current type itself.
      String propertyTypeString = propertyType == this ? "{...}" : propertyType.toString();
      sb.append(propertyTypeString);

      ++i;
      if (i >= MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    prettyPrint = true; // Restore pretty print
    return sb.toString();
  } else {
    return "{...}";
  }
}
