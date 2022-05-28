package cz.swisz.so.task5;

import java.util.ArrayList;

public abstract class DistributionSimulation {
    private final ArrayList<CPU> _cpus;
    private final ArrayList<Process> _tasks;
    private int _migrationCount;
    private double _totalTime;

    public DistributionSimulation(ArrayList<Process> taskList, int cpuCount) {
        _cpus = new ArrayList<>();
        _tasks = taskList;
        _totalTime = 0.0;

        for (int i = 0; i < cpuCount; i++)
            _cpus.add(new CPU());
    }

    protected void migrateProcess(Process proc, CPU destination) {
        proc._owner.removeProcess(proc);
        destination.assignNewProcess(proc);
        _migrationCount++;
    }

    public int getMigrationCount() {
        return _migrationCount;
    }

    public double getTotalTime() {
        return _totalTime;
    }

    public void start() {
        double time = 0.0;
        int index = 0;

        while (index < _tasks.size()) {
            Process nextProcess = _tasks.get(index);

            double nextEventTime = 1e10;
            for (CPU c : _cpus) {
                double cpuEvent = c.getNextEventTime();
                if (cpuEvent < nextEventTime)
                    nextEventTime = cpuEvent;
            }

            if (nextEventTime < nextProcess._appearTime) { // One of the process will end earlier than next process starts
                time = nextEventTime;
                for (CPU c : _cpus) {
                    c.update(time);
                }
            } else { // Next process will start before any of the current running process will end
                time = nextProcess._appearTime;
                for (CPU c : _cpus) {
                    c.update(time);
                }

                _cpus.get(nextProcess._cpuId).assignNewProcess(nextProcess);
                handleNewProcess(nextProcess, _cpus.get(nextProcess._cpuId), _cpus);
                index++;
            }
        }

        double nextEventTime = 0;
        while (nextEventTime < 1e6){
            nextEventTime = 1e10;
            for (CPU c : _cpus) {
                double cpuEvent = c.getNextEventTime();
                if (cpuEvent < nextEventTime)
                    nextEventTime = cpuEvent;
            }

            if (nextEventTime < 1e6) {
                _totalTime = nextEventTime;
                for (CPU c : _cpus) {
                    c.update(nextEventTime);
                }
            }
        }
    }

    public ArrayList<CPU> getCpus() {
        return _cpus;
    }

    protected void incrementMigrations() {
        _migrationCount++;
    }

    public abstract void handleNewProcess(Process proc, CPU assignedTo, ArrayList<CPU> allCpus);
}
