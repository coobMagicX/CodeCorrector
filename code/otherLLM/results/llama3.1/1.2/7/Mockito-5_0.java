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
        catch (org.mockito.exceptions.verification.MockitoException e) { // <--- CHANGED TO MockitoException
            if (e.getMessage().contains("JUnit")) { // <--- ADDED CHECK FOR JUNIT-RELATED ERROR
                continue;
            } else {
                throw e; // <--- PROPAGATE RELEVANT ERRORS
            }
        }
    }

    if (error != null) {
        throw error;
    }
}