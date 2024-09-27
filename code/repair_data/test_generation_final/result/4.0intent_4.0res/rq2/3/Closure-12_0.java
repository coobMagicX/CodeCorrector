private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) return false;
    
    // Assuming Node can have multiple children and each child could be a try-catch block
    for (Node child : cfgNode.getChildren()) {
        if (child.getType() == Node.Type.TRY_CATCH_BLOCK) {
            return true; // Found a try-catch block
        }
    }

    return false; // No try-catch block found
}