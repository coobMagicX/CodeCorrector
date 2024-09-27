private boolean isInlinableObject(List<Reference> refs) {
    boolean ret = false;
    Set<String> validProperties = Sets.newHashSet();
    Set<String> deletedProperties = Sets.newHashSet();

    for (Reference ref : refs) {
        Node name = ref.getNode();
        Node parent = ref.getParent();
        Node gramps = ref.getGrandparent();

        if (parent.isGetProp()) {
            Preconditions.checkState(parent.getFirstChild() == name);

            if (gramps.isCall() && gramps.getFirstChild() == parent) {
                return false;
            }

            String propName = parent.getLastChild().getString();
            if (validProperties.contains(propName) || deletedProperties.contains(propName)) {
                continue;
            }

            if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
                validProperties.add(propName);
            } else {
                deletedProperties.add(propName);
                return false;
            }
        } else {
            if (!isVarOrAssignExprLhs(name)) {
                return false;
            }

            Node val = ref.getAssignedValue();
            if (val == null || !val.isObjectLit()) {
                return false;
            }

            for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
                if (child.isGetterDef() || child.isSetterDef()) {
                    return false;
                }

                validProperties.add(child.getString());

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

            ret = true;
        }
    }
    return ret;
}