import java.util.Set;
import java.util.TreeSet;

String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  } else if (prettyPrint) {
    prettyPrint = false; // Turn off pretty printing to avoid infinite recursion

    // Use a TreeSet to ensure properties are sorted; explicitly specify type to maintain Java 6 compatibility
    Set<String> propertyNames = new TreeSet<String>();
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
      // Directly use property type's `toString` to respect its pretty printing implementation; assuming it handles internal recursion appropriately.
      sb.append(getPropertyType(property).toString());

      ++i;
      if (i >= MAX_PRETTY_PRINTED_PROPERTIES) {
        sb.append(", ...");
        break;
      }
    }

    sb.append("}");

    prettyPrint = true;  // Restore pretty print state.
    return sb.toString();
  } else {
    return "{...}";
  }
}
