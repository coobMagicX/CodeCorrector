private static boolean isReduceableFunctionExpression(Node n) {
    // Check if it's a function expression and not a property getter
    if (NodeUtil.isFunctionExpression(n)) {
        return true;
    } else if (NodeUtil.isPropertyAccess(n)) {
        // If it's a property access, check if the property is being accessed repeatedly
        // This could be replaced with more sophisticated logic depending on actual Node structure
        return false; // Assuming repeated property getter access is not reducible
    }
    return false;
}