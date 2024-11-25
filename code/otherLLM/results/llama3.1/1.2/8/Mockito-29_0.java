public void describeTo(Description description) {
    if (wanted != null) { // Added validation for null input
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    } else {
        description.appendText("null"); // Handle potential NPE scenario by describing the object as "null"
    }
}