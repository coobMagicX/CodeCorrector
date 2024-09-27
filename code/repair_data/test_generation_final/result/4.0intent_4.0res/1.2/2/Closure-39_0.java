import java.util.HashSet;
import java.util.Set;

public class ObjectType {
    private boolean prettyPrint = true;
    private static final int MAX_PRETTY_PRINTED_PROPERTIES = 10;

    // Assuming the existence of these methods based on context
    boolean hasReferenceName() {
        return false; // Simplified example logic
    }

    String getReferenceName() {
        return "ExampleReference"; // Simplified example logic
    }

    ObjectType getImplicitPrototype() {
        return null; // Simplified example logic
    }

    boolean isNativeObjectType() {
        return false; // Simplified example logic
    }

    Set<String> getOwnPropertyNames() {
        return new HashSet<>(); // Simplified example logic
    }

    public JSType getPropertyType(String property) {
        StaticSlot<JSType> slot = getSlot(property);
        if (slot == null) {
            return getNativeType(JSTypeNative.UNKNOWN_TYPE);
        }
        return slot.getType();
    }

    StaticSlot<JSType> getSlot(String property) {
        return null; // Simplified example logic
    }

    JSType getNativeType(JSTypeNative type) {
        return new JSType(); // Simplified example logic
    }

    // Modified toStringHelper to handle recursive references
    String toStringHelper(boolean forAnnotations) {
        return toStringHelper(forAnnotations, new HashSet<>());
    }

    private String toStringHelper(boolean forAnnotations, Set<ObjectType> visited) {
        if (hasReferenceName()) {
            return getReferenceName();
        } else if (prettyPrint) {
            // Detect recursive references
            if (visited.contains(this)) {
                return "{...}";
            }
            visited.add(this);

            // Don't pretty print recursively.
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
                sb.append(getPropertyType(property).toString());

                ++i;
                if (i == MAX_PRETTY_PRINTED_PROPERTIES) {
                    sb.append(", ...");
                    break;
                }
            }

            sb.append("}");

            prettyPrint = true;
            visited.remove(this);
            return sb.toString();
        } else {
            return "{...}";
        }
    }
}