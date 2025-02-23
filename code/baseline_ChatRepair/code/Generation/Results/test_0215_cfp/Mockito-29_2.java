public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description);
    description.appendText(String.valueOf(wanted));  
    appendQuoting(description);
    description.appendText(")");
