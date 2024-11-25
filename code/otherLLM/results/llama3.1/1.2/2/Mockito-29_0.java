public void describeTo(Description description) {
    if (null == wanted) { // Added null check for input parameter 'wanted'
        description.appendText("same(null)"); // Handle null value
    } else {
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    }
}