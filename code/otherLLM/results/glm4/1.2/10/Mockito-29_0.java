public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description);
    
    // Check if 'wanted' is null before calling toString()
    if (wanted != null) {
        description.appendText(wanted.toString());
    }
    
    appendQuoting(description);
    description.appendText(")");
}