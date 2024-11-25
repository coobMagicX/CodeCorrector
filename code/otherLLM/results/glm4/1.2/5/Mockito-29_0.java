public void describeTo(Description description) {
    if (wanted == null) {
        description.appendText("same(null)");
    } else {
        description.appendText("same(");
        appendQuoting(description);
        try {
            String wantedString = wanted.toString();
            if (wantedString != null) {
                description.appendText(wantedString);
            }
        } catch (Exception e) {
            description.appendText("[toString() threw exception]");
        }
        appendQuoting(description);
        description.appendText(")");
    }
}

private void appendQuoting(Description description) {
    // Existing implementation of appendQuoting to handle quoting logic
}