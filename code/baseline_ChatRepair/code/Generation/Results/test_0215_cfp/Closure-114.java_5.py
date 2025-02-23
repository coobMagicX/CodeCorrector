private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    
    if (ns != null) {
        Node grandParent = parent.getParent();
        // Determine if the current node is within a 'for' loop but not 'for-in' or 'for-of' loop
        if (parent.isFor() && !NodeUtil.isForIn(parent) && !NodeUtil.isForOf(parent)) {
            // Handle FOR loop structures to avoid claiming loop structure as dependency incorrectly
            Node initializer = parent.getFirstChild();
            if (initializer == n || initializer.getFirstChild() == n) {
                // Covers cases where the assignment happens in the init part of the for loop
                recordDepScope(nameNode, ns);
            } else {
                // Considering general cases such as condition or increment parts of the for loop
                recordDepScope(grandParent, ns);
            }
        } else {
            // Check all ancestor nodes up to a function or script node
            boolean dependencyRecorded = false;
            for (Node ancestor = parent; ancestor != null; ancestor = ancestor.getParent()) {
                if (ancestor.isCall()) {
                    // If assignment is part of a function call, associate with the call node
                    recordDepScope(ancestor.getSecondChild(), ns);
                    dependencyRecorded = true;
                    break;
                } else if (ancestor.isFunction()) {
                    // If under a function but not a part of a call, associate with the function itself
                    recordDepScope(ancestor, ns);
                    dependencyRecorded = true;
                    break;
                }
            }
            if (!dependencyRecorded) {
                // As a fallback, use the recorded node for dependency
                recordDepScope(recordNode, ns);
            }
        }
    }
}
