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
        } catch (org.mockito.exceptions.base.MockitoAssertionError e) { // Catch the general Mockito assertion error
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}
