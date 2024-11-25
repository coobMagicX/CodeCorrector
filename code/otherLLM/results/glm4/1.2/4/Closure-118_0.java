private void handleObjectLit(NodeTraversal t, Node n) {
  StringBuilder objectBuilder = new StringBuilder("{");
  boolean firstProperty = true;

  for (Node child = n.getFirstChild();
       child != null;
       child = child.getNext()) {
    // Maybe STRING, GET, SET

    // We should never see a mix of numbers and strings.
    String name = child.getString();
    T type = typeSystem.getType(getScope(), n, name);

    Property prop = getProperty(name);
    if (prop == null) continue; // Skip undefined properties

    if (!firstProperty) {
      objectBuilder.append(",");
    } else {
      firstProperty = false;
    }

    String value;
    try {
      value = processPropertyValue(t, child, type);
    } catch (Exception e) {
      compiler.report(JSError.make(
          t.getSourceName(), child, "Error processing property value", Warnings.VALIDATION_ERROR,
          name, null, n.toString(), ""));
      continue; // Skip this property if there's an error
    }

    objectBuilder.append("\"")
                 .append(name)
                 .append("\":")
                 .append(value);
  }
  objectBuilder.append("}");
  
  // Now we have the JSON string representation of the object
  String jsonString = objectBuilder.toString();
  System.out.println(jsonString); // For demonstration purposes

  if (!jsonString.equals("{")) {
    compiler.report(JSError.make(
        t.getSourceName(), n, "Object literal is not correctly formatted", Warnings.INVALIDATION,
        "Format Error", null, n.toString(), ""));
  }
}