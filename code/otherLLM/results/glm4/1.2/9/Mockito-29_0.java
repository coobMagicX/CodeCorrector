public void describeTo(Description description) {
    if (description == null) {
        throw new IllegalArgumentException("Description cannot be null");
    }
    
    description.appendText("same(");
    appendQuoting(description);
    if (wanted != null) {
        description.appendText(wanted.toString());
    } else {
        description.appendText("null"); // Handle the case where 'wanted' is null
    }
    appendQuoting(description);
    description.appendText(")");
}

// Assuming the method appendQuoting exists and has been defined elsewhere in your code:
private void appendQuoting(Description description) {
    if (description == null) {
        throw new IllegalArgumentException("Description cannot be null");
    }
    
    // Implementation of the quoting logic
}