String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    boolean hasRecursiveReference = false;
    Set<String> propertyNames = Sets.newTreeSet();
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType() &&
        !hasRecursiveReference) {
      if (propertyNames.size() > 0) {
        hasRecursiveReference = true;
      }
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

    if (hasRecursiveReference) {
      // Add a flag to indicate that the output has been truncated due to recursion.
      sb.append(" (TRUNCATED DUE TO RECURSION)");
    }

    sb.append("}");

    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}