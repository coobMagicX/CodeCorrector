public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            } else {
                error = null;
            }
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        } catch (Exception e) { // Catching a generic exception to avoid JUnit dependency
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}
