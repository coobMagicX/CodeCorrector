public void describeTo(Description description) {
    if (description == null || wanted == null) {
        return; // Early return to avoid NullPointerException
    }
    description.appendText("same(");
    appendQuoting(description);
    description.appendText(wanted.toString());
    appendQuoting(description);
    description.appendText(")");
}

private void appendQuoting(Description description) {
    if (wanted instanceof String) {
        description.appendText("\"");
    } else if (wanted instanceof Character) {
        description.appendText("'");
    }
}