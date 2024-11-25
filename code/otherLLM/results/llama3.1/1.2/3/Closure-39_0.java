String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    // Don't pretty print recursively.
    boolean recursive = false;
    ObjectType current = this;
    while (current != null && !current.isNativeObjectType()) {
      if (current.getImplicitPrototype() == this) {
        recursive = true;
        break;
      }
      current = current.getImplicitPrototype();
    }

    if (!recursive) {
      prettyPrint = false;

      // Use a tree set so that the properties are sorted.
      Set<String> propertyNames = Sets.newTreeSet();
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
        sb.append(getPropertyType(property).toString());

        ++i;
        if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
          sb.append(", ...");
          break;
        }
      }

      sb.append("}");
    } else {
      // If it's a recursive record, print the reference to the parent record
      return getReferenceName() + " with reference to " + this.getImplicitPrototype().getReferenceName();
    }

    prettyPrint = true;
    return sb.toString();
  } else {
    return "{...}";
  }
}