private void handleObjectLit(NodeTraversal t, Node n) {
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        // Ensure child is of type STRING, GET, or SET which can have meaningful property names
        if (child.isString() || child.isGetProp() || child.isSetProp()) {
            String name = child.getString();  // Get the property name from the child node
            T type = typeSystem.getType(getScope(), n, name);  // Determine the type of the property

            Property prop = getProperty(name);  // Retrieve the property object
            if (!prop.scheduleRenaming(child, processProperty(t, prop, type, null))) {
                // If renaming is not scheduled, check if an error should be reported
                if (propertiesToErrorFor.containsKey(name)) {
                    compiler.report(JSError.make(
                        t.getSourceName(), child, propertiesToErrorFor.get(name),
                        Warnings.INVALIDATION, name,
                        (type == null ? "null" : type.toString()), n.toString(), ""));
                }
            }
        }
    }
}