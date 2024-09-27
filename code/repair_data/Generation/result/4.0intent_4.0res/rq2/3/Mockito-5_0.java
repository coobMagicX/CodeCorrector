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
        // Removed specific dependency on JUnit by catching a more general exception
        catch (AssertionError e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}