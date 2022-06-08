package model;

public interface OnTimeTick {
    void onTimePass(int secondsSinceLastCall);
}
