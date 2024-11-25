private void addJsString(Node n) {
    cc.addString(n.getValue());
}

private double getSimpleNumber(String key) {
    try {
        return Double.parseDouble(key);
    } catch (NumberFormatException e) {
        // If the string cannot be parsed to a number, return NaN.
        return Double.NaN;
    }
}

private void addAllSiblings(Node n) {
    for (Node c = n; c != null; c = c.getNext()) {
        cc.listSeparator();
        add(c);
    }
}

private void addCaseBody(Node n) {
    if (n.isGetterDef() || n.isSetterDef()) {
        add(n);
    } else {
        Preconditions.checkState(n.isStringKey());
        String key = n.getString();
        // Handle the case body here...
    }
}