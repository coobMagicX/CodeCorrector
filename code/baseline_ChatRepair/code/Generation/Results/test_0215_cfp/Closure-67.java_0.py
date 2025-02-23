private boolean isPrototypePropertyAssign(Node assign) {
    if (assign.isAssign()) {
        Node leftNode = assign.getFirstChild();
        if (leftNode != null && leftNode.isGetProp()) {
            Node prototypeNode = leftNode.getFirstChild();
            if (prototypeNode != null && prototypeNode.isGetProp()) {
                Node objectNode = prototypeNode.getFirstChild();
                Node propertyNode = prototypeNode.getNext();
                
                if (propertyNode != null && propertyNode.isString() 
                    && "prototype".equals(propertyNode.getString())) {
                    return true;
                }
            }
        }
    }
    return false;
}
