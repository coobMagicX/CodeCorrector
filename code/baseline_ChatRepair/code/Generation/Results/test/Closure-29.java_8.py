private boolean isInlinableObject(List<Reference> refs) {
    if (refs.isEmpty()) {
        return false; // Return false when there are no references.
    }

    for (Reference ref : refs) {
        Node nameNode = ref.getNode();
        Node parent = ref.getParent();
        
        // Check for direct assignments or variable references.
        if (!(nameNode.isName() && (NodeUtil.isVarDeclaration(parent) || isSimpleAssignToName(parent)))) {
            return false;
        }

        Node assignmentValue = nameNode.hasChildren() ? nameNode.getFirstChild() : null;

        // Check that the assignment is an object literal.
        if (assignmentValue == null || !assignmentValue.isObjectLit()) {
            return false;
        }

        // Check for self-referential assignments within the object literal.
        if (containsSelfReference(assignmentValue, nameNode.getString())) {
            return false;
        }
    }

    return true; // All references validated
}

private boolean isSimpleAssignToName(Node node) {
    return node.isAssign() && node.getFirstChild().isName();
}

private boolean containsSelfReference(Node objectLit, String varName) {
    for (Node key = objectLit.getFirstChild(); key != null; key = key.getNext()) {
        Node value = key.getFirstChild();
        if (value != null && value.isName() && value.getString().equals(varName)) {
            return true; // Self-reference found.
        }
    }
    return false;
}
