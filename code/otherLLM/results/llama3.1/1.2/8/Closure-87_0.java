/**
 * Checks if a block can be folded into an expression.
 *
 * @param block the block to check
 * @return true if the block can be folded, false otherwise
 */
private boolean isFoldableExpressionBlock(Block block) {
    // Check for simple assignments or call expressions
    Node node = block.getFirstChild();
    while (node != null && !node.isLeaf()) {
        node = node.getFirstChild();
    }
    
    // If we have a leaf node, it's an expression
    if (node == null || node.getType() == Token.NAME) {
        return true;
    }

    // Check for more complex expressions that can be folded
    switch (node.getType()) {
        case Token.ASSIGN:
            // Assignment to a variable or a property
            return true;
        case Token.CALL:
            // Function call with no side effects
            return true;
        default:
            break;
    }

    // If none of the above conditions are met, it's not an expression block
    return false;
}