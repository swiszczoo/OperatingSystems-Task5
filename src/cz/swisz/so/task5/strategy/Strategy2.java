package cz.swisz.so.task5.strategy;

import cz.swisz.so.task5.CPU;
import cz.swisz.so.task5.DistributionSimulation;
import cz.swisz.so.task5.Process;

import java.util.ArrayList;
import java.util.Random;

public class Strategy2 extends DistributionSimulation {
    private final Random _random;
    private final double _p;
    private final int _z;

    public Strategy2(ArrayList<Process> taskList, int cpuCount, double p, int z) {
        super(taskList, cpuCount);

        _random = new Random();
        _p = p;
        _z = z;
    }

    @Override
    public void handleNewProcess(Process proc, CPU assignedTo, ArrayList<CPU> allCpus) {
        if (assignedTo.getRealLoadNoCount() > _p) {
            for (int i = 0; i < _z; i++) {
                int cpuToAsk = _random.nextInt(allCpus.size());
                CPU dest = allCpus.get(cpuToAsk);

                if (dest.getRealLoad() < _p) {
                    migrateProcess(proc, dest);

                    break;
                }
            }
        }
    }
}
