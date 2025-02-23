private boolean isInlinableObject(List<Reference> refs) {
    boolean ret = false;
    Set<String> validProperties = Sets.newHashSet();
    Set<String> deletedProperties = new HashSet<>();

    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();

        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);
            if (gramps.isCall() && gramps.getFirstChild() == parent) {
                return false;
            }

            // Check if deleting property
            if (gramps.isDelProp()) {
                deletedProperties.add(parent.getLastChild().getString());
                return false;
            }

            String propName = parent.getLastChild().getString();
            if (deletedProperties.contains(propName)) {
                return false;
            }

            if (!validProperties.contains(propName)) {
                if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
                    validProperties.add(propName);
                } else {
                    return false;
                }
            }
            continue;
        }

        if (!NodeUtil.isVarOrSimpleAssignLhs(name, parent)) {
            return false;
        }

        Node val = ref.getAssignedValue();
        if (val == null) {
            continue;
        }

        if (!val.isObjectLit()) {
            return false;
        }

        for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isGetterDef() || child.isSetterDef()) {
                return false;
            }

            String childPropName = child.getString();
            
            // Ensure not to inline properties that are deleted or will be deleted
            if (deletedProperties.contains(childPropName)) {
                return false;
            }

            validProperties.add(childPropName);

            Node childVal = child.getFirstChild();
            for (Reference t : refs) {
                Node refNode = t.getParent();
                while (!NodeUtil.isStatementBlock(refNode)) {
                    if (refNode == childVal) {
                        return false;
                    }
                    refNode = refNode.getParent();
                }
            }
        }

        ret = true; // We have an object literal assignment suitable for inlining
    }
    return ret;
}
