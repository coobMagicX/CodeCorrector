String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Temporarily disable pretty print to prevent recursive pretty printing.
    prettyPrint = false;
    
    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = new TreeSet<>();
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

      sb.append(property);
      sb.append(": ");
      // Instead of using `getPropertyType(property).toString()`, use a special method that doesn't recurse fully.
      sb.append(getPropertyType(property).toAnnotationString()); // Change .toString() to handle recursive types

      ++i;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    // Re-enable pretty printing after this instance's processing.
    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}
