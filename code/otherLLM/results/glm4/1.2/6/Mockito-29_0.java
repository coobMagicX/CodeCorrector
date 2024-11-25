public void describeTo(Description description) {
    if (wanted == null) {
        description.appendText("same(null)");
    } else {
        description.appendText("same\"");
        appendQuoting(description);
        description.appendText(wanted.toString());
        appendQuoting(description);
        description.appendText(")");
    }
}

// Assuming the methods appendQuoting and appendText exist in some context
private void appendQuoting(Description description) {
    // Implementation of appendQuoting, which should handle quoting logic.
}

private void appendText(Description description) {
    // Implementation of appendText, which should handle appending text to Description instance.
}