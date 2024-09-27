String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
        return getReferenceName();
    } else if (prettyPrint) {
        // Don't pretty print recursively.
        prettyPrint = false;

        // Use a tree set so that the properties are sorted.
        Set<String> propertyNames = Sets.newTreeSet();
        Set<ObjectType> visitedTypes = new HashSet<>(); // Track visited types to handle recursion

        for (ObjectType current = this;
             current != null && !current.isNativeObjectType() &&
                     propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES &&
                     visitedTypes.add(current); // Add current type to visited types
             current = current.getImplicitPrototype()) {
            propertyNames.addAll(current.getOwnPropertyNames());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        int i = 0;
        int propertiesPrinted = 0; // Track the number of properties printed
        for (String property : propertyNames) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(property);
            sb.append(": ");
            sb.append(getPropertyType(property).toString());

            ++i;
            ++propertiesPrinted;
            if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
        }

        // Append ", ..." if more properties exist
        if (propertiesPrinted < propertyNames.size()) {
            sb.append(", ...");
        }

        sb.append("}");

        prettyPrint = true;
        return sb.toString();
    } else {
        return "{...}";
    }
}