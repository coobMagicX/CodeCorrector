private boolean isSafeReplacement(Node node, Node replacement) {
    
    if (node == null || replacement == null) {
        return false;  
    }
  
    
    if (node.isName() && replacement.isName()) {
        return true;
    }
  
    
    if (node.is