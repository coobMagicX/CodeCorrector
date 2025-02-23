String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Temporarily disable pretty printing to prevent recursive pretty printing.
    prettyPrint = false;
    
    // Use a tree set with explicit generic type specification to ensure compatibility with Java 6.
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
      // Call toString on the property’s type. Avoid recursive pretty printing by managing flags properly.
      try {
        sb.append(getPropertyType(property).toStringHelper(false)); // Handle recursion especially for complex structure
      } catch (StackOverflowError e) {
        sb.append("...");
      }

      ++i;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    // Re-enable pretty printing after this instance's processing is completed
    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}
