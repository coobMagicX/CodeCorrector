private boolean isPrototypePropertyAssign(Node assign) {
    if (assign == null || !assign.isAssign()) {
        return false;
    }

    Node lhs = assign.getFirstChild();  // Left hand side of the assignment
    if (lhs != null && lhs.isGetProp()) {
        Node lastGetProp = lhs;
        while (lastGetProp.getFirstChild() != null && lastGetProp.getFirstChild().isGetProp()) {
            lastGetProp = lastGetProp.getFirstChild();
        }

        if (lastGetProp.getFirstChild() != null && lastGetProp.getSecondChild().isString()) {
            return "prototype".equals(lastGetProp.getSecondChild().getString());
        }
    }
    return false;
}

