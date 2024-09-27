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
      if (isPropertyRecursive(property)) {
        sb.append("{...}");
      } else {
        sb.append(getPropertyType(property).toString());
      }

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
    return "{...}";
  }
}

boolean isPropertyRecursive(String property) {
  ObjectType current = this;
  while (current != null && !current.isNativeObjectType()) {
    if (current.getOwnPropertyNames().contains(property)) {
      return true;
    }
    current = current.getImplicitPrototype();
  }
  return false;
}

Set<String> getOwnPropertyNames() {
  Set<String> propertyNames = new HashSet<>();
  for (StaticSlot<JSType> slot : propertySlots) {
    propertyNames.add(slot.getName());
  }
  return propertyNames;
}