package com.test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

public class CpuTets {

	
	private static CpuTets instance = new CpuTets();

    private OperatingSystemMXBean osMxBean;
    private ThreadMXBean threadBean;
    private long preTime = System.nanoTime();
    private long preUsedTime = 0;

    private CpuTets() {
        osMxBean = ManagementFactory.getOperatingSystemMXBean();
        threadBean = ManagementFactory.getThreadMXBean();
    }
    
    public static CpuTets getInstance() {
        return instance;
    }

    public double getProcessCpu() {
        long totalTime = 0;
        for (long id : threadBean.getAllThreadIds()) {
            totalTime += threadBean.getThreadCpuTime(id);
        }
        long curtime = System.nanoTime();
        long usedTime = totalTime - preUsedTime;
        long totalPassedTime = curtime - preTime;
        preTime = curtime;
        preUsedTime = totalTime;
        return (((double) usedTime) / totalPassedTime / osMxBean.getAvailableProcessors()) * 100;
    }
	
	
}
