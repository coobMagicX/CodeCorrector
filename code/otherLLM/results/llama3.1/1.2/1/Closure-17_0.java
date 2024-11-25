private JSType getDeclaredType(String sourceName, JSDocInfo info) {
    if (info.isFunction()) {
        return info.getReturnType();
    } else if (isCollapsibleValue(info.getNode(), false)) {
        Node value = getValue(info.getNode());
        if (value != null) {
            // Check if the type of the value is already known
            JSType type = getJSType(value);
            if (type != null) {
                return type;
            }
        }
    }

    // If no other conditions match, just return the original type
    return info.getType();
}

private Node getValue(Node node) {
    // Check if the value of this expression is already known
    for (Node child : getChildren(node)) {
        if (isCollapsibleValue(child, true)) {
            Node value = getValue(child);
            if (value != null) {
                return value;
            }
        }
    }
    return null;
}