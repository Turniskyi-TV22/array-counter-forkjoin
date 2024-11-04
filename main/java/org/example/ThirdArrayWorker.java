package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;

public class ThirdArrayWorker  extends RecursiveTask<CopyOnWriteArrayList<Integer>> {

    private CopyOnWriteArrayList<Integer> list;

    public ThirdArrayWorker(CopyOnWriteArrayList<Integer> array) {
        this.list = array;
    }

    @Override
    protected CopyOnWriteArrayList<Integer> compute() {
        if(list.size() <= 1) {
            if(list.getFirst() < 10 || list.getFirst() > 175){
                list.removeFirst();
            }
            return list;
        }
        ThirdArrayWorker firstHalf = new ThirdArrayWorker(new CopyOnWriteArrayList<>(list.subList(0, list.size() / 2)));
        ThirdArrayWorker secondHalf = new ThirdArrayWorker(new CopyOnWriteArrayList<>(list.subList(list.size() / 2, list.size())));

        firstHalf.fork();
        secondHalf.fork();
        CopyOnWriteArrayList<Integer> result1 = firstHalf.join();
        CopyOnWriteArrayList<Integer> result2 = secondHalf.join();
        result1.addAll(result2);
        return result1;
    }
}
