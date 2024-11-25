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

            String type = getPropertyType(property).toString();
            // Format the string differently based on whether annotations are needed.
            sb.append(property);
            sb.append(": ");
            if (forAnnotations) {
                sb.append("@").append(type); // Assuming annotation format uses '@' prefix.
            } else {
                sb.append(type);
            }

            ++i;
            if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
        }

        sb.append("}");

        prettyPrint = true; // Reset prettyPrint to true after formatting
        return sb.toString();
    } else {
      return "{...}";
    }
}