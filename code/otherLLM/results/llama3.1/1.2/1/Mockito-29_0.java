public void describeTo(Description description) {
    if (wanted == null) {
        description.appendText("null");
    } else {
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    }
}