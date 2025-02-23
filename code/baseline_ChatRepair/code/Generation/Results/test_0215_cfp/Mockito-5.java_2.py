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
        } catch (Exception e) { // Now catching generic Exception
            // Wrap or translate the exception into an AssertionError
            error = new AssertionError("Verification failed due to an exception: " + e.getMessage(), e);
        }
    }

    if (error != null) {
        throw error;
    }
}
