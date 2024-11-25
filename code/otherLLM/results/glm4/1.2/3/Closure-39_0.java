import java.util.HashSet;
import java.util.Set;

String toStringHelper(boolean forAnnotations) {
  if (hasReferenceName()) {
    return getReferenceName();
  }

  StringBuilder sb = new StringBuilder();

  // Use a HashSet to keep track of visited objects.
  Set<Object> visitedObjects = new HashSet<>();
  StringBuilder propertiesBuffer = new StringBuilder();
  boolean firstProperty = true;

  // Start with the current object.
  Object currentObject = this;
  while (currentObject != null && !visitedObjects.contains(currentObject)) {
    if (!currentObject.getClass().isArray() && !isNativeObjectType(currentObject)) {
      for (String property : getOwnPropertyNames(currentObject)) {
        if (!visitedObjects.contains(getProperty(property, currentObject))) {
          // Check for circular reference to avoid infinite recursion.
          if (firstProperty) {
            sb.append("{");
            firstProperty = false;
          } else {
            sb.append(", ");
          }

          sb.append(property).append(": ").append(getPropertyType(property));

          // Add the object that holds this property to the visited set.
          visitedObjects.add(getProperty(property, currentObject));
        }
      }
    }
    if (currentObject != null) {
      currentObject = getImplicitPrototype(currentObject);
    }
  }

  if (!firstProperty) {
    sb.append("}");
  } else {
    // If no properties were added, we still need to return something.
    sb.append("{...}");
  }

  return forAnnotations ? annotateProperties(sb.toString()) : sb.toString();
}

// Helper methods that should be defined in the context:
// boolean hasReferenceName();
// String getReferenceName();
// Set<String> getOwnPropertyNames(Object object);
// Object getProperty(String property, Object object);
// String getPropertyType(String property);
// Object getImplicitPrototype(Object object);
// String annotateProperties(String propertiesString);
// boolean isNativeObjectType(Object object);