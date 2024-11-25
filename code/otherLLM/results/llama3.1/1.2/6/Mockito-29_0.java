public void describeTo(Description description) {
    if (wanted != null && actual != null) { // added null check for inputs
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    } else if (wanted == null && actual == null) {
        description.appendText("same(null)");
    }
}

public boolean matches(Object actual) {
    return wanted == actual;
}