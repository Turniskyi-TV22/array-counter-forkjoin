package org.example;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;

public class FirstArrayWorker extends RecursiveTask<CopyOnWriteArrayList<Integer>> {

    private CopyOnWriteArrayList<Integer> list;

    public FirstArrayWorker(CopyOnWriteArrayList<Integer> array) {
        this.list = array;
    }

    @Override
    protected CopyOnWriteArrayList<Integer> compute() {
        if (list.size() == 1) {
            list.set(0, list.get(0) * 3);
            return list;
        }

        FirstArrayWorker firstHalf = new FirstArrayWorker(new CopyOnWriteArrayList<>(list.subList(0, list.size() / 2)));
        FirstArrayWorker secondHalf = new FirstArrayWorker(new CopyOnWriteArrayList<>(list.subList(list.size() / 2, list.size())));


        firstHalf.fork();
        secondHalf.fork();

        CopyOnWriteArrayList<Integer> result1 = firstHalf.join();
        CopyOnWriteArrayList<Integer> result2 = secondHalf.join();

        result1.addAll(result2);
        return result1;
    }
}
