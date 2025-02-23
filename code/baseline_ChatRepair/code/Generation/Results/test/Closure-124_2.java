private boolean isSafeReplacement(Node node, Node replacement) {
    
    if (node == null || replacement == null) {
        return false;
    }
    
    
    if (node.isName()) {
        
        return !node.getString().equals(replacement.getQualifiedName());
    }
    
    