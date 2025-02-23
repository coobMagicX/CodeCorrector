// Maintain a stack to handle recursive pretty printing states.
private final Deque<Boolean> prettyPrintStack = new ArrayDeque<>();

String toStringHelper(boolean forAnnotations) {
    if (hasReferenceName()) {
        return getReferenceName();
    } else if (prettyPrint) {
        // Push the current pretty print state and set it to false
        prettyPrintStack.push(prettyPrint);
        prettyPrint = false;

        // Use a tree set so that the properties are sorted.
        Set<String> propertyNames = new TreeSet<>();
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
            sb.append(getPropertyType(property).toStringHelper(false)); // use helper with non-pretty print mode

            ++i;
            if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                sb.append(", ...");
                break;
            }
        }

        sb.append("}");

        // Restore the pretty print state from stack
        prettyPrint = prettyPrintStack.pop();
        return sb.toString();
    } else {
        return "{...}";
    }
}
