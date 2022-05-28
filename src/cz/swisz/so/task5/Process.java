package cz.swisz.so.task5;

public class Process implements Comparable<Process> {
    CPU _owner;
    public int _cpuId;
    public double _cpuLoad;
    public double _appearTime;
    public double _executionTime;
    public double _timeRemaining;

    public Process(int cpuId, double cpuLoad, double appearTime, double executionTime) {
        _owner = null;
        _cpuId = cpuId;
        _cpuLoad = cpuLoad;
        _appearTime = appearTime;
        _executionTime = executionTime;
        _timeRemaining = executionTime;
    }

    @Override
    public int compareTo(Process o) {
        return Double.compare(_appearTime, o._appearTime);
    }

    Process cloneProcess() {
        return new Process(_cpuId, _cpuLoad, _appearTime, _executionTime);
    }
}
