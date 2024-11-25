String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    // If there's a reference name, just use it
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    prettyPrint = false;

    Set<String> propertyNames = Sets.newTreeSet();
    for (ObjectType current = this;
         current != null && !current.isNativeObjectType() &&
             propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES;
         current = current.getImplicitPrototype()) {
      if (!current.isSelfReference()) { // handle recursive references
        propertyNames.addAll(current.getOwnPropertyNames());
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

    // format properties for annotations
    sb.append(" [annotated]"); // add annotation information

    sb.append("}");

    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}