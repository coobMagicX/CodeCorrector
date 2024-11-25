public void describeTo(Description description) {
    if (description == null) {
        throw new IllegalArgumentException("The Description object cannot be null");
    }
    
    description.appendText("same(");
    appendQuoting(description);
    // Check for null before calling toString() on wanted to prevent NullPointerException
    if (wanted != null) {
        description.appendText(wanted.toString());
    } else {
        description.appendText("null"); // Handling the case when 'wanted' is null
    }
    
    appendQuoting(description);
    description.appendText(")");
}