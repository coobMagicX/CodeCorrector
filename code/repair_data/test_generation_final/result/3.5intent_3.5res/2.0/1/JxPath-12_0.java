public static boolean testNode(Node node, NodeTest test) {
    if (test == null) {
        return true;
    }

    if (test instanceof NodeNameTest) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return false;
        }

        NodeNameTest nodeNameTest = (NodeNameTest) test;
        QName testName = nodeNameTest.getNodeName();
        String namespaceURI = nodeNameTest.getNamespaceURI();
        boolean wildcard = nodeNameTest.isWildcard();
        String testPrefix = testName.getPrefix();
        
        if (wildcard && testPrefix == null) {
            return true;
        }
        
        if (wildcard || testName.getName().equals(DOMNodePointer.getLocalName(node))) {
            String nodeNS = DOMNodePointer.getNamespaceURI(node);
            if (wildcard && (testPrefix == null || testPrefix.isEmpty())) {
                return equalStrings(namespaceURI, nodeNS);
            } else {
                String nodePrefix = node.getPrefix();
                return equalStrings(namespaceURI, nodeNS) && equalStrings(testPrefix, nodePrefix);
            }
        }
        
        return false;
    }

    // Rest of the method remains unchanged
}