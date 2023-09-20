package com.usecase;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ConferenceRoomDeadlock {
	private static final Object doorLock = new Object();
    private static final Object keyLock = new Object();
    
    public static void main(String[] args) throws  InterruptedException {
        Thread personA = new Thread(() -> {
            synchronized (keyLock) {
                System.out.println("Person A has the key.");
                synchronized (doorLock) {
                    System.out.println("Person A enters the conference room.");
                }
            }
        });

        Thread personB = new Thread(() -> {
            synchronized (doorLock) {
                System.out.println("Person B is at the door.");
                synchronized (keyLock) {
                    System.out.println("Person B enters the conference room.");
                }
            }
        });

        personA.start();
        personB.start();
     // Check for deadlocks
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        while (true) {
            long[] threadIds = threadMXBean.findDeadlockedThreads();
            if (threadIds != null) {
                ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);
                System.out.println("Deadlock detected!");
                for (ThreadInfo threadInfo : threadInfos) {
                    System.out.println("The " + threadInfo.getThreadName() + " is deadlocked.");
                }
                break;
            }
            Thread.sleep(1000); // Wait and check again
            
        }
    }
}
