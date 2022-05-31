package cz.swisz.so.task5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Generator {
    static void generateProcessList(ArrayList<Process> list, int cpuCount, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int cpuId = random.nextInt(cpuCount);
            double cpuLoad = random.nextDouble() * 0.5;
            double appearTime = random.nextDouble() * count / 50.0;
            double executionTime = random.nextDouble() * 15 + 0.5;

            list.add(new Process(cpuId, cpuLoad, appearTime, executionTime));
        }

        list.sort(Comparator.naturalOrder());
    }
}
