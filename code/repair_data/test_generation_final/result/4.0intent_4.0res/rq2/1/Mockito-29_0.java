public void describeTo(Description description) {
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