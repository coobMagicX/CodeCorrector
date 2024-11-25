private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode.hasChildren() && cfgNode.getChildren().size() == 1) {
        Node child = cfgNode.getChildren().get(0);
        return child.getName().equals("try") || child.getName().equals("catch");
    } else if (cfgNode.getName().equals("function")) {
        // Check if the function call is an exception handler
        String funcName = cfgNode.getAttributeValue("name").getValue();
        if (funcName.equals("someFunction") && cfgNode.getAttributeValue("params").getValues().size() == 1) {
            return true;
        }
    }

    return false;
}