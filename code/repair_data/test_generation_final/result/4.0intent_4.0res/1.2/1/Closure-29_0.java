private boolean isInlinableObject(List<Reference> refs) {
    boolean ret = false;
    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();

        // Ignore indirect references, like x.y (except x.y(), since
        // the function referenced by y might reference 'this').
        if (parent.isGetProp() && gramps.isCall() && gramps.getFirstChild() == parent) {
            // A call target maybe using the object as a 'this' value.
            return false;
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
        for (Node child = val.getFirstChild(); child != null;
             child = child.getNext()) {
            if (child.isGetterDef() || child.isSetterDef()) {
                // ES5 get/set not supported.
                return false;
            }

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

        // Check if the object literal has all properties defined on it
        // directly, and not on its prototype.
        Node prototypeProperty = val.getProp(NodeUtil.getPrototypePropertyName());
        if (prototypeProperty != null && prototypeProperty.isObjectLit()) {
            // Object prototype has properties which could lead to errors
            return false;
        }

        // We have found an acceptable object literal assignment.
        ret = true;
    }
    return ret;
}