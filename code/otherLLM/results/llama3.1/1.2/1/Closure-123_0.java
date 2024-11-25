private class Property {
    // Assuming 'type' is an enum value and 'n' is a Node object as defined in the original code

    private void addSwitchStatement(Node n) {
        if (n.getType() == Token.SWITCH) {
            add("switch(");
            add(n.getFirstChild());
            add(")");
            cc.beginBlock();
            addAllSiblings(n.getNext());
            cc.endBlock(context == Context.STATEMENT);
        }
    }

    private void addCaseBody(Node n) {
        if (n.getType() == Token.CASE || n.getType() == Token.DEFAULT_CASE) {
            add(":");
            addNonEmptyStatement(
                    n.getFirstChild(), getContextForNonEmptyExpression(Context.OTHER), true);
        } else if (n.isGetterDef() || n.isSetterDef()) {
            add(n);
        }
    }

    private void addJsString(Node n) {
        // Assuming 'n' is a Node object representing a string
        cc.addString(n.getString());
    }

    private double getSimpleNumber(String key) {
        try {
            return Double.parseDouble(key);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}