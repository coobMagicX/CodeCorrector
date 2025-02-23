private boolean isPrototypePropertyAssign(Node assign) {
    if (assign != null && assign.getFirstChild() != null) {
        Node leftNode = assign.getFirstChild();
        
        if (leftNode.getType() == Token.GETPROP) {
            Node target = leftNode.getFirstChild();
            Node prop = leftNode.getLastChild();
            
            if (prop != null && prop.isString() 
                && "prototype".equals(prop.getString())) {
                return true;
            }
        }
    }
    return false;
}
