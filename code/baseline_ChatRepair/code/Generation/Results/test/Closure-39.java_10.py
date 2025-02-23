String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
        return getReferenceName();
    } else if (prettyPrint) {
        // Use a tree set so that the properties are sorted.
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
        prettyPrint = false;  // Temporarily turn off pretty printing

        for (String property : propertyNames) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(property);
            sb.append(": ");
            // Call toStringHelper on each property to avoid reentrant pretty print
            sb.append(getPropertyType(property).toStringHelper(false));

            ++i;
            if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
        }

        sb.append("}");

        prettyPrint = true;  // Turn pretty printing back on
        return sb.toString();
    } else {
        return "{...}";
    }
}
