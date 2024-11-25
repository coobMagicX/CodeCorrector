public void describeTo(Description description) {
    if (wanted == null) { // add a check for null input
        description.appendText("same(null)");
    } else {
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    }
}