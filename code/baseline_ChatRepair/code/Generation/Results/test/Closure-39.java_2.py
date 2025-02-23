String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Temporarily disable pretty printing to avoid recursion
    prettyPrint = false;

    // Use a TreeSet to ensure properties are sorted, specify the type explicitly for Java 6 compatibility
    Set<String> propertyNames = new TreeSet<String>();
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
      JSType propertyType = getPropertyType(property);
      
      // Handle self-reference recursively by using {...}
      if (this.equals(propertyType)) {
        sb.append("{...}");
      } else {
        sb.append(propertyType.toString());
      }

      i++;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    // Re-enable pretty printing on exit from this block
    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}
