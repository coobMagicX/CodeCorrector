String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Save the original state of prettyPrint
    boolean originalPrettyPrint = prettyPrint;
    // Turn off prettyPrint to avoid infinite recursion
    prettyPrint = false;

    // Use a TreeSet to ensure properties are sorted
    Set<String> propertyNames = new TreeSet<>();
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
      
      // Check for self-reference to prevent recursion
      if (this.equals(propertyType)) {
        sb.append("{...}");
      } else {
        // Temporarily disable pretty print for properties if it was initially on
        propertyType.setPrettyPrint(false);
        sb.append(propertyType.toString());
        // Restore the state of pretty print
        propertyType.setPrettyPrint(originalPrettyPrint);
      }

      i++;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");
    // Restore the original state of prettyPrint
    prettyPrint = originalPrettyPrint;
    return sb.toString();
  } else {
    return "{...}";
  }
}
