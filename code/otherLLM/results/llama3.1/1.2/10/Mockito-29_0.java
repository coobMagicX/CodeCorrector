public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description, wanted); // modified line
    description.appendText(")");
}

private void appendQuoting(Description description, Object obj) {
    if (obj == null) { // added null check
        return; // ignore null inputs
    }
    if (wanted instanceof String) {
        description.appendText("\"");
    } else if (wanted instanceof Character) {
        description.appendText("'");
    }
}