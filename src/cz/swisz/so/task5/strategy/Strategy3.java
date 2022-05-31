package cz.swisz.so.task5.strategy;

import cz.swisz.so.task5.CPU;
import cz.swisz.so.task5.DistributionSimulation;
import cz.swisz.so.task5.Process;

import java.util.ArrayList;
import java.util.Random;

public class Strategy3 extends DistributionSimulation {
    private final Random _random;
    private final double _p, _r;

    public Strategy3(ArrayList<Process> taskList, int cpuCount, double p, double r) {
        super(taskList, cpuCount);

        _random = new Random();
        _p = p;
        _r = r;
    }

    @Override
    public void handleNewProcess(Process proc, CPU assignedTo, ArrayList<CPU> allCpus) {
        for (CPU cpu : allCpus) {
            if (cpu.getRealLoadNoCount() < _r) {
                int selectedId = _random.nextInt(allCpus.size());
                CPU selected = allCpus.get(selectedId);

                if (selected.getRealLoad() > _p) {
                    ArrayList<Process> releasedTasks = selected.releaseTasks();
                    for (Process p : releasedTasks) {
                        cpu.assignNewProcess(p);
                        incrementMigrations();
                    }
                }
            }
        }
    }
}
