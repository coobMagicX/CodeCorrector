public void verify(VerificationData data) {
    AssertionError error = null;

    timer.start();
    while (timer.isCounting()) {
        try {
            delegate.verify(data);

            if (returnOnSuccess) {
                return;
            }
        } catch (MockitoAssertionError e) {
            error = handleVerifyException(e);
        } catch (ArgumentsAreDifferent e) { // Exception from Mockito not JUnit
            error = handleVerifyException(e);
        } catch (AssertionError e) { // This is more general and does not depend on JUnit
            error = handleVerifyException(e);
        }
    }

    if (error != null) {
        throw error;
    }
}
