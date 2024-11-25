public void describeTo(Description description) {
    if (wanted != null) { // added input validation
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    } else {
        description.appendText("same(null)"); // handle null input by appending "null"
    }
}