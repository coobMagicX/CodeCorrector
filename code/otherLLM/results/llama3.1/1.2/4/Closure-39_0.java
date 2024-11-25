String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    prettyPrint = false;

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType() &&
           propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES) {
      if (!propertyNames.contains(current.getReferenceName())) { // Check for recursive reference
        propertyNames.addAll(current.getOwnPropertyNames());
      } else {
        break; // Break the loop when a recursive reference is found
      }
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
  } else {
    // If the object has a recursive reference, display it as {...}
    if (hasReferenceName() && !getImplicitPrototype().isNativeObjectType()) {
      return "{...}";
    } else {
      return "{...}";
    }
  }
}