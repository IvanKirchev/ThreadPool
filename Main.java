import java.util.*;

import static java.lang.Thread.currentThread;

public class Main {
    static class ThreadPool {
        Queue<Runnable> tasks = new LinkedList<>();
        List<Thread> workers = new ArrayList<>();

        public ThreadPool(int size) {
            initWorkers(size);
        }

        public void execute(Runnable newTask) {
            tasks.add(newTask);
        }

        private synchronized Runnable pollNextTask() {
            return tasks.poll();
        }
        
        private void initWorkers(int size) {
            for (int i = 0; i < size; i++) {
                String name = "Worker" + (i+1);
                Thread worker = new Thread(() -> {
                    while (true) {
                        Runnable newTaskToBeExecuted = pollNextTask();
                        if (newTaskToBeExecuted != null) newTaskToBeExecuted.run();
                    }
                }, name);
                workers.add(worker);
                worker.start();
            }
        }
    }

    public static void main(String[] args) {
        ThreadPool myThreadPool = new ThreadPool(10);

        for (int i = 0; i < 100; i++) {
            final String taskName = "task" + (i+1);

            Runnable task = () -> {
                System.out.println(taskName + " running in thread: " + currentThread().getName());
                try {
                    System.out.println(taskName + " started at " + new Date().getTime());
                    Thread.sleep(((int) (Math.random() * 1000) % 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println( taskName +" ended at " + new Date().getTime());
            };
            
            myThreadPool.execute(task);
        }
    }
}
