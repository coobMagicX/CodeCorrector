private void handleObjectLit(NodeTraversal t, Node n) {
    for (Node child = n.getFirstChild();
         child != null;
         child = child.getNext()) {
        // Maybe STRING, GET, SET

        String name = child.getString();
        T type = typeSystem.getType(getScope(), n, name);

        Property prop = getProperty(name);
        if (prop == null) {
            compiler.report(JSError.make(
                t.getSourceName(), child, "undefined property", Warnings.INVALIDATION, name,
                (type == null ? "null" : type.toString()), n.toString(), ""));
            continue;
        }

        boolean shouldContinue = prop.scheduleRenaming(child, processProperty(t, prop, type, null));
        if (!shouldContinue) {
            // If renaming is not scheduled, report an error
            if (propertiesToErrorFor.containsKey(name)) {
                compiler.report(JSError.make(
                    t.getSourceName(), child, propertiesToErrorFor.get(name),
                    Warnings.INVALIDATION, name,
                    (type == null ? "null" : type.toString()), n.toString(), ""));
            }
        }
    }
}