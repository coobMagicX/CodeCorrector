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
        catch (org.mockito.exceptions.verification.junit.ArgumentsAreDifferent e) {
            error = handleVerifyException(e);
        }
        // Catch and handle JUnit-related exceptions
        catch (java.lang.RuntimeException re) {
            if (re.getCause() instanceof org.junit.runner.notification.RunListenerJUnitException) {
                error = new AssertionError("JUnit exception caught during verification", re);
            } else {
                error = new AssertionError("Unknown JUnit exception caught during verification", re);
            }
        }
    }

    if (error != null) {
        throw error;
    }
}