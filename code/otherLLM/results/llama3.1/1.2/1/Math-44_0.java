public class Integrator {
    // ...

    private boolean statesInitialized = false;
    private List<StepHandler> stepHandlers = new ArrayList<>();
    private List<EventState> eventsStates = new ArrayList<>();

    public void initializeEventHandlers() {
        if (stepHandlers.isEmpty()) {
            // Add default event handlers here
        }
        // ...
    }

    public void addEventHandler(StepHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Step handler cannot be null");
        }
        stepHandlers.add(handler);
    }

    public void addEventState(EventState state) {
        if (state == null) {
            throw new NullPointerException("Event state cannot be null");
        }
        eventsStates.add(state);
    }

    // ...
}

public class EventState {
    private double eventTime;

    public boolean evaluateStep(AbstractStepInterpolator interpolator) {
        if (interpolator.getGlobalPreviousTime() >= this.eventTime) {
            return false;
        }
        // Evaluate the event
        return true;
    }

    public void stepAccepted(double time, double[] state) {
        // Handle the event
    }

    public boolean reset(double time, double[] state) {
        // Reset the state if needed
        return false;
    }
}

public class StepHandler {
    public void handleStep(AbstractStepInterpolator interpolator, double[] state) {
        // Handle the step
    }
}