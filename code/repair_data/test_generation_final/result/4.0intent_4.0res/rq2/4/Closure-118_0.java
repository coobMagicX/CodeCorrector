private void handleObjectLit(NodeTraversal t, Node n) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        // Maybe STRING, GET, SET

        // We should never see a mix of numbers and strings.
        String name = child.getString();
        T type = typeSystem.getType(getScope(), n, name);

        // Construct a unique property identifier using node's unique name and the property name
        String uniquePropertyName = n.getQualifiedName() + "." + name;

        Property prop = getProperty(uniquePropertyName);
        if (!prop.scheduleRenaming(child,
                                   processProperty(t, prop, type, null))) {
          // TODO(user): It doesn't look like the user can do much in this
          // case right now.
          if (propertiesToErrorFor.containsKey(uniquePropertyName)) {
            compiler.report(JSError.make(
                t.getSourceName(), child, propertiesToErrorFor.get(uniquePropertyName),
                Warnings.INVALIDATION, uniquePropertyName,
                (type == null ? "null" : type.toString()), n.toString(), ""));
          }
        }
    }
}