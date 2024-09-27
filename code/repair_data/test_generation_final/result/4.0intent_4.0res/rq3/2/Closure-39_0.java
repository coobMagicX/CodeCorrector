import java.util.Set;
import java.util.TreeSet;

class ObjectType {
    private static final int MAX_PRETTY_PRINTED_PROPERTIES = 10;
    private boolean prettyPrint = true; // default value if needed
    private ObjectType implicitPrototype;
    private Set<String> ownPropertyNames;
    
    // Dummy method implementations for demonstration
    public boolean hasReferenceName() { return false; }
    public String getReferenceName() { return "ReferenceName"; }
    public boolean isNativeObjectType() { return false; }
    public ObjectType getImplicitPrototype() { return implicitPrototype; }
    public Set<String> getOwnPropertyNames() { return ownPropertyNames; }
    public String getPropertyType(String property) { return "TypeOf" + property; }

    public String toStringHelper(boolean forAnnotations) {
        if (hasReferenceName()) {
            return getReferenceName();
        } else if (prettyPrint) {
            prettyPrint = false; // Disable pretty print temporarily

            Set<String> propertyNames = new TreeSet<>(); // Use tree set for sorted properties
            ObjectType current = this;
            while (current != null && !current.isNativeObjectType() &&
                    propertyNames.size() <= MAX_PRETTY_PRINTED_PROPERTIES) {
                propertyNames.addAll(current.getOwnPropertyNames());
                current = current.getImplicitPrototype();
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
                sb.append(getPropertyType(property));

                ++i;
                if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                    sb.append(", ...");
                    break;
                }
            }

            sb.append("}");

            prettyPrint = true; // Re-enable pretty printing
            return sb.toString();
        } else {
            return "{...}";
        }
    }
}