private boolean isPrototypePropertyAssign(Node assign) {
    if (assign != null && assign.getFirstChild() != null) {
        Node leftNode = assign.getFirstChild();
        
        // Ensure the left node of the assignment is a property access (GETPROP)
        if (leftNode.getType() == Token.GETPROP) {
            Node target = leftNode.getFirstChild();
            
            // Ensure the target of the GETPROP is itself a GETPROP chain
            if (target != null && target.getType() == Token.GETPROP) {
                Node prop = target.getFirstChild();
                
                // Check the string identifier of the deepest node in GETPROP chain
                // to see if it is "prototype"
                if (prop.getNext() != null && 
                    prop.getNext().getType() == Token.STRING &&
                    "prototype".equals(prop.getNext().getString())) {
                    return true;
                }
            }
        }
    }
    return false;
}
