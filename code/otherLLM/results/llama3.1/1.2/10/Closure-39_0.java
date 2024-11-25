String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    prettyPrint = false;

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    for (ObjectType current = this;
         current != null && !current.isNativeObjectType() &&
             propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES;
         current = current.getImplicitPrototype()) {
      // Incorporate referenced types
      Set<String> currentProperties = current.getOwnPropertyNames();
      for (String propertyName : currentProperties) {
        if (!propertyNames.contains(propertyName)) {
          propertyNames.add(propertyName);
        }
      }
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

    // Correct handling of recursive records
    if (propertyNames.size() > MAX_PRETTY_PRINTED_PROPERTIES) {
      sb.replace(sb.indexOf("..."), sb.length(), "...");
      sb.append("[cycle detected]");
    }

    sb.append("}");

    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}