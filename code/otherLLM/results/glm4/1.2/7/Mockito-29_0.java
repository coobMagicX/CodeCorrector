public void describeTo(Description description) {
    if (description == null) {
        // Handle the case where description is null, for example by throwing an exception or logging
        throw new IllegalArgumentException("The Description object cannot be null.");
    }
    
    description.appendText("same(");
    appendQuoting(description);
    if (wanted != null) { // Check to ensure wanted is not null before calling toString()
        description.appendText(wanted.toString());
    } else {
        description.appendText("null"); // Appending "null" to represent a null value
    }
    appendQuoting(description);
    description.appendText(")");
}