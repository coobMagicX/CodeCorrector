public void describeTo(Description description) {
    if (description == null) {
        return; // Early exit to prevent NullPointerException if description is null.
    }
    
    description.appendText("same(");
    appendQuoting(description); // This method should be designed to handle nulls.
    if (wanted != null) { // Check for null before calling toString() on wanted.
        description.appendText(wanted.toString());
    } else {
        description.appendText("null"); // Provide a default value or behavior for null.
    }
    appendQuoting(description); // This method should also be designed to handle nulls.
    description.appendText(")");
}