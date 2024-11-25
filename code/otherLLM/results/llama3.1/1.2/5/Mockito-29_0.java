public void describeTo(Description description) {
    description.appendText("same(");
    if (wanted != null) {
        appendQuoting(description, wanted.toString());
    } else {
        description.appendText("<null>");
    }
    description.appendText(")");
}