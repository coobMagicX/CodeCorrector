private boolean isInlinableObject(List<Reference> refs) {
    boolean inlinableFound = false;
    for (Reference ref : refs) {
        Node nameNode = ref.getNode();
        Node parent = ref.getParent();
        Node grandparent = ref.getGrandparent();

        // If the reference isn't a direct assignment or variable declaration, it's not inlinable.
        if (!isValidReferenceContext(parent)) {
            return false;
        }

        Node valueNode = ref.getAssignedValue();
        if (valueNode == null) {
            continue;
        }

        if (!valueNode.isObjectLit()) {
            return false;
        }

        if (hasSelfReferentialAssignment(valueNode, nameNode)) {
            return false;
        }

        if (hasGetOrSetMethods(valueNode)) {
            return false;
        }

        inlinableFound = true;
    }
    return inlinableFound;
}

private boolean isValidReferenceContext(Node parent) {
    return (parent.isVar() || (parent.isAssign() && parent.getParent().isExprResult()));
}

private boolean hasSelfReferentialAssignment(Node objectLiteral, Node target) {
    for (Node keyNode = objectLiteral.getFirstChild(); keyNode != null; keyNode = keyNode.getNext()) {
        Node value = keyNode.getFirstChild();
        if (referencesNode(value, target)) {
            return true;
        }
    }
    return false;
}

private boolean referencesNode(Node expression, Node target) {
    if (expression == target) {
        return true;
    }

    for (Node child = expression.getFirstChild(); child != null; child = child.getNext()) {
        if (referencesNode(child, target)) {
            return true;
        }
    }
    return false;
}

private boolean hasGetOrSetMethods(Node objectLiteral) {
    for (Node property = objectLiteral.getFirstChild(); property != null; property = property.getNext()) {
        if (property.isGetterDef() || property.isSetterDef()) {
            return true;
        }
    }
    return false;
}
