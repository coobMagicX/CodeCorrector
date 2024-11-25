String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
      return getReferenceName();
    } else if (prettyPrint) {
      // Don't pretty print recursively.
      boolean wasPrettyPrint = prettyPrint;
      prettyPrint = false;

      Set<ObjectType> visitedObjects = Sets.newHashSet(); // Keep track of visited objects to prevent infinite recursion
      StringBuilder sb = new StringBuilder();
      
      // Use a tree set so that the properties are sorted and start with current object
      Set<String> propertyNames = Sets.newTreeSet();
      ObjectType current = this;
      while (current != null && !visitedObjects.contains(current) && !current.isNativeObjectType()) {
        visitedObjects.add(current);
        propertyNames.addAll(current.getOwnPropertyNames());
        current = current.getImplicitPrototype();
      }
      
      sb.append("{");
      int i = 0;
      for (String property : propertyNames) {
        if (i > 0) {
          sb.append(", ");
        }
        sb.append(property);
        sb.append(": ");

        // Check for recursion in the value
        Object value = current.getPropertyValue(property);
        if (value instanceof ObjectType && ((ObjectType) value).equals(this)) {
          sb.append("[Circular Reference]");
        } else {
          sb.append(value != null ? value.toString() : "null");
        }

        ++i;
        if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
          sb.append(", ...");
          break;
        }
      }
      sb.append("}");

      prettyPrint = wasPrettyPrint; // Restore the original prettyPrint state
      return sb.toString();
    } else {
      return "{...}";
    }
}