public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            }
        } catch (MockitoAssertionError e) { // Mockito internal assertion error handles all Mockito-related assertion problems
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}
