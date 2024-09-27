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
        }
        catch (ArgumentsAreDifferent e) { // Modified to remove JUnit dependency
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}