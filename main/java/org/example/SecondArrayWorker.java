package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;

public class SecondArrayWorker extends RecursiveTask<CopyOnWriteArrayList<Integer>> {

    private CopyOnWriteArrayList<Integer> list;

    public SecondArrayWorker(CopyOnWriteArrayList<Integer> array) {
        this.list = array;
    }

    @Override
    protected CopyOnWriteArrayList<Integer> compute() {
        if(list.size() <= 1) {
            //System.out.println("Array2: " + list.getFirst());
            //System.out.println("Array2: " + (list.getFirst() % 2 != 0));
            if(list.getFirst() % 2 != 0){
                list.removeFirst();
            }
            return list;
        }
        SecondArrayWorker firstHalf = new SecondArrayWorker(new CopyOnWriteArrayList<>(list.subList(0, list.size() / 2)));
        SecondArrayWorker secondHalf = new SecondArrayWorker(new CopyOnWriteArrayList<>(list.subList(list.size() / 2, list.size())));


        firstHalf.fork();
        secondHalf.fork();

        CopyOnWriteArrayList<Integer> result1 = firstHalf.join();
        CopyOnWriteArrayList<Integer> result2 = secondHalf.join();

        result1.addAll(result2);
        return result1;
    }
}
