package model;

/**
 * Represents a valve on the system which gets updated on a time base.
 */
public class Valve implements OnTimeTick {
    private ValveState state;
    private ValveListener listener;
    /**
     * flow l/time
     */
    private double flow;

    public Valve(double flow) {
        state = ValveState.CLOSED;
        this.flow = flow;
    }

    public void setState(ValveState state) {
        this.state = state;
    }

    public ValveState getState() {
        return state;
    }

    public void setListener(ValveListener listener) {
        this.listener = listener;
    }

    @Override
    public void onTimePass(int secondsSinceLastCall) {
        if(state == ValveState.OPEN || state == ValveState.STUCK_OPEN)
            if(listener != null) listener.onValveUpdate(this.flow * secondsSinceLastCall);
    }
}
