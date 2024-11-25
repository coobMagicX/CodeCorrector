public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            getDelegate().verify(data); // Use the getDelegate() method to access the delegate

            if (returnOnSuccess) {
                return;
            } else {
                error = null; // Correctly assign null after a successful verification
            }
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        } catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}