private boolean isInlinableObject(List<Reference> refs) {
    // Check all references to assess if the object is suited for inlining.
    for (Reference ref : refs) {
        Node nameNode = ref.getNode();
        Node assignmentNode = nameNode.getParent();

        // Ensure that the parent node of the name node is an assignment, and the name node is the target of the assignment
        if (!assignmentNode.isAssign() || !assignmentNode.getFirstChild().equals(nameNode)) {
            return false;
        }

        Node valueNode = assignmentNode.getSecondChild();

        // Ensure that the assigned value is an object literal
        if (!valueNode.isObjectLit()) {
            return false;
        }

        // Check to ensure no self-references within the object literal which could form cyclic dependencies
        if (hasSelfReferentialProperties(valueNode, refs)) {
            return false;
        }
    }

    return true; // Passes all checks, so object can be inlined
}

private boolean hasSelfReferentialProperties(Node objectLit, List<Reference> refs) {
    for (Node property = objectLit.getFirstChild(); property != null; property = property.getNext()) {
        Node valueNode = property.getSecondChild();

        // Check if valueNode references any of the nodes within the refs list
        for (Reference ref : refs) {
            Node refNode = ref.getNode();
            if (refNode.isName() && valueNode.isName() && valueNode.getString().equals(refNode.getString())) {
                return true; // Found a self-reference
            }
        }
    }
    return false;
}
