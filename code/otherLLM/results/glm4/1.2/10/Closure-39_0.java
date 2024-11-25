public String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    boolean originalPrettyPrint = prettyPrint;
    prettyPrint = false;

    StringBuilder sb = new StringBuilder();
    Stack<ObjectType> prototypeChain = new Stack<>();
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType() && prototypeChain.isEmpty()) {
      prototypeChain.push(current);
      current = current.getImplicitPrototype();
    }

    // Use a tree set so that the properties are sorted.
    Set<String> propertyNames = Sets.newTreeSet();
    for (ObjectType object : prototypeChain) {
      propertyNames.addAll(object.getOwnPropertyNames());
    }

    sb.append("{");

    int i = 0;
    for (String property : propertyNames) {
      if (i > 0) {
        sb.append(", ");
      }
      
      // Append property name and its type
      sb.append(property);
      sb.append(": ");

      JSType propertyType = getPropertyType(property);
      String propertyTypeName = propertyType == null ? "null" : propertyType.getTypeName();
      if (propertyTypeName.equals("ProxyObjectType")) {
        // Detect a recursive reference to the current object
        if (!prototypeChain.contains(this)) {
          sb.append("this");
        } else {
          sb.append(propertyTypeName); // Use the unknown type symbol for self-referencing properties
        }
      } else {
        sb.append(propertyTypeName);
      }

      ++i;
      if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    prettyPrint = originalPrettyPrint; // Restore the original pretty print setting
    return sb.toString();
  } else {
    return "{...}";
  }
}