import java.util.HashSet;
import java.util.Set;

public class ObjectType {
    private boolean hasReferenceName;
    private String referenceName;
    private boolean prettyPrint;
    private ObjectType implicitPrototype;
    private static final int MAX_PRETTY_PRINTED_PROPERTIES = 10;
    private Set<String> ownPropertyNames;
    private boolean isNativeObjectType;

    public ObjectType() {
        this.hasReferenceName = false;
        this.referenceName = "";
        this.prettyPrint = true;
        this.implicitPrototype = null;
        this.ownPropertyNames = new HashSet<>();
        this.isNativeObjectType = false;
    }

    public boolean hasReferenceName() {
        return hasReferenceName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public boolean isNativeObjectType() {
        return isNativeObjectType;
    }

    public Set<String> getOwnPropertyNames() {
        return ownPropertyNames;
    }

    public ObjectType getImplicitPrototype() {
        return implicitPrototype;
    }

    public JSType getPropertyType(String property) {
        // Implementation provided by existing methods (mocked for this context)
        return new JSType(property);
    }

    String toStringHelper(boolean forAnnotations, Set<ObjectType> visited) {
        if (hasReferenceName()) {
            return getReferenceName();
        } else if (prettyPrint) {
            // Don't pretty print recursively.
            prettyPrint = false;
            Set<String> propertyNames = new HashSet<>();

            if (visited == null) {
                visited = new HashSet<>();
            }

            if (visited.contains(this)) {
                prettyPrint = true;
                return "{...}";
            }

            visited.add(this);

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

class JSType {
    private String type;

    public JSType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}