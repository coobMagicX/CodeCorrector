private boolean isInlinableObject(List<Reference> refs) {
    boolean ret = false;
    Set<String> validProperties = Sets.newHashSet();
    Set<String> deletedProperties = Sets.newHashSet(); // Track deleted properties

    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();

        // Ignore most indirect references, like x.y (but not x.y(),
        // since the function referenced by y might reference 'this').
        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);
            // A call target may be using the object as a 'this' value.
            if (gramps.isCall() && gramps.getFirstChild() == parent) {
                return false;
            }

            String propName = parent.getLastChild().getString();
            // Check if the property was marked as deleted
            if (deletedProperties.contains(propName)) {
                return false;
            }

            if (!validProperties.contains(propName)) {
                if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
                    validProperties.add(propName);
                } else {
                    deletedProperties.add(propName); // Mark as deleted if not a valid assignment
                    return false;
                }
            }
            continue;
        }

        // Handle delete operations
        if (parent.isDelProp()) {
            String propName = parent.getLastChild().getString();
            deletedProperties.add(propName); // Add property to deleted set
            continue; // Skip further checks for this property
        }

        // Only rewrite VAR declarations or simple assignment statements
        if (!isVarOrAssignExprLhs(name)) {
            return false;
        }

        Node val = ref.getAssignedValue();
        if (val == null) {
            // A var with no assignment.
            continue;
        }

        // We're looking for object literal assignments only.
        if (!val.isObjectLit()) {
            return false;
        }

        // Make sure that the value is not self-referential. IOW,
        // disallow things like x = {b: x.a}.
        for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isGetterDef() || child.isSetterDef()) {
                // ES5 get/set not supported.
                return false;
            }
            
            validProperties.add(child.getString());

            Node childVal = child.getFirstChild();
            // Check if childVal is the parent of any of the passed in
            // references, as that is how self-referential assignments
            // will happen.
            for (Reference t : refs) {
                Node refNode = t.getParent();
                while (!NodeUtil.isStatementBlock(refNode)) {
                    if (refNode == childVal) {
                        // There's a self-referential assignment
                        return false;
                    }
                    refNode = refNode.getParent();
                }
            }
        }

        // We have found an acceptable object literal assignment. As
        // long as there are no other assignments that mess things up,
        // we can inline.
        ret = true;
    }
    return ret;
}