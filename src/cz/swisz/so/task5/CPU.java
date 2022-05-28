package cz.swisz.so.task5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CPU {
    private final List<Process> _runningTasks;
    private double _load;
    private double _time;
    private double _avgLoadSum;
    private int _loadRequests;

    public CPU() {
        _runningTasks = new LinkedList<>();
        _load = 0.0;
        _time = 0.0;
        _avgLoadSum = 0.0;
        _loadRequests = 0;
    }

    public int getLoadRequestCount() {
        return _loadRequests;
    }

    public double getRealLoad() {
        _loadRequests++;
        return getRealLoadNoCount();
    }

    public double getRealLoadNoCount() {
        return Math.min(_load, 1.0);
    }

    public double getTimeScale() {
        return Math.max(_load, 1.0);
    }

    public double getAverageLoad() {
        if (_time == 0.0)
            return 0.0;
        return _avgLoadSum / _time;
    }

    public double getNextEventTime() {
        double minTime = 1e10;

        for (Process p : _runningTasks) {
            if (p._timeRemaining < minTime) {
                minTime = p._timeRemaining;
            }
        }

        return _time + minTime * getTimeScale();
    }

    public void update(double newTime) {
        if (newTime < _time)
            throw new IllegalArgumentException("newTime must be >= than current time");

        double interval = newTime - _time;
        if (interval == 0)
            return;

        double timePassed = interval / getTimeScale(); // If we have more than 100% load, everhing will slow down
        _avgLoadSum += interval * getRealLoadNoCount();

        ListIterator<Process> it = _runningTasks.listIterator();
        while(it.hasNext()) {
            Process p = it.next();
            p._timeRemaining -= timePassed;

            if (Math.abs(p._timeRemaining) < 1e-8) {
                _load -= p._cpuLoad;
                p._owner = null;
                it.remove();
            } else if (p._timeRemaining < 0.0) {
                throw new RuntimeException("we overlooked at least one process :(");
            }
        }

        _time = newTime;
    }

    public void assignNewProcess(Process p) {
        if (p._owner != null) {
            throw new RuntimeException("process already has an owner");
        }

        _runningTasks.add(p);
        _load += p._cpuLoad;
        p._owner = this;
    }

    public void removeProcess(Process p) {
        if (_runningTasks.contains(p)) {
            p._owner = null;
            _load -= p._cpuLoad;
            _runningTasks.remove(p);
        }
    }

    public ArrayList<Process> releaseTasks() {
        double targetLoad = _load * 2.0 / 3.0;
        ArrayList<Process> released = new ArrayList<>();

        ListIterator<Process> it = _runningTasks.listIterator();
        while (_load > targetLoad && it.hasNext()) {
            Process p = it.next();
            _load -= p._cpuLoad;
            p._owner = null;
            released.add(p);
            it.remove();
        }

        return released;
    }
}
