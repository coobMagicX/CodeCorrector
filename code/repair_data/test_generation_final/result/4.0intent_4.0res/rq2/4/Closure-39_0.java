String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
        return getReferenceName();
    } else if (prettyPrint) {
        // Don't pretty print recursively.
        prettyPrint = false;

        // Use a tree set so that the properties are sorted.
        Set<String> propertyNames = new TreeSet<>();
        for (ObjectType current = this;
             current != null && !current.isNativeObjectType();
             current = current.getImplicitPrototype()) {
            propertyNames.addAll(current.getOwnPropertyNames());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        int i = 0;
        for (String property : propertyNames) {
            if (i >= MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(property);
            sb.append(": ");
            sb.append(getPropertyType(property).toString());

            ++i;
        }

        sb.append("}");

        prettyPrint = true;
        return sb.toString();
    } else {
        return "{...}";
    }
}