package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        //STEP 1
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<CopyOnWriteArrayList<Integer>> futureArray1 = executor.submit(() -> updateFile(generateRandomArray(),"array1.txt"));
        Future<CopyOnWriteArrayList<Integer>> futureArray2 = executor.submit(() ->updateFile(generateRandomArray(),"array2.txt"));
        Future<CopyOnWriteArrayList<Integer>> futureArray3 = executor.submit(() ->updateFile(generateRandomArray(),"array3.txt"));

        CopyOnWriteArrayList<Integer> array1 = futureArray1.get();
        CopyOnWriteArrayList<Integer> array2 = futureArray2.get();
        CopyOnWriteArrayList<Integer> array3 = futureArray3.get();

        System.out.println("Array1: " + array1);
        System.out.println("Array2: " + array2);
        System.out.println("Array3: " + array3);

        //STEP 2
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        CopyOnWriteArrayList<Integer> processedArray1 = forkJoinPool.invoke(new FirstArrayWorker(array1));
        CopyOnWriteArrayList<Integer> processedArray2 = forkJoinPool.invoke(new SecondArrayWorker(array2));
        CopyOnWriteArrayList<Integer> processedArray3 = forkJoinPool.invoke(new ThirdArrayWorker(array3));
        futureArray1 = executor.submit(() -> updateFile(processedArray1,"array1.txt"));
        futureArray2 = executor.submit(() ->updateFile(processedArray2,"array2.txt"));
        futureArray3 = executor.submit(() ->updateFile(processedArray3,"array3.txt"));
        array1 = futureArray1.get();
        array2 = futureArray2.get();
        array3 = futureArray3.get();

        System.out.println("Array1 after worker: " + array1);
        System.out.println("Array2 after worker: " + array2);
        System.out.println("Array3 after worker: " + array3);

        //STEP 3
        Collections.sort(array1);
        CopyOnWriteArrayList<Integer> sortedArray1 = array1;
        Collections.sort(array2);
        CopyOnWriteArrayList<Integer> sortedArray2 = array2;
        Collections.sort(array3);
        CopyOnWriteArrayList<Integer> sortedArray3 = array3;
        futureArray1 = executor.submit(() -> updateFile(sortedArray1,"array1.txt"));
        futureArray2 = executor.submit(() ->updateFile(sortedArray2,"array2.txt"));
        futureArray3 = executor.submit(() ->updateFile(sortedArray3,"array3.txt"));
        array1 = futureArray1.get();
        array2 = futureArray2.get();
        array3 = futureArray3.get();

        System.out.println("Array1 after sorting: " + array1);
        System.out.println("Array2 after sorting: " + array2);
        System.out.println("Array3 after sorting: " + array3);

        //STEP 4
        CopyOnWriteArrayList<Integer> array4 = mergeArrays(array1, array2, array3);
        Future<CopyOnWriteArrayList<Integer>> futureArray4 = executor.submit(() ->updateFile(array4,"array4.txt"));

        executor.shutdown();
    }

    private static CopyOnWriteArrayList<Integer> generateRandomArray() {
        Random random = new Random();
        int size = 15 + random.nextInt(11);
        CopyOnWriteArrayList<Integer> array = new CopyOnWriteArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(random.nextInt(1001));
        }
        return array;
    }

    private static void writeArrayToFile(List<Integer> list, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Integer item : list) {
                writer.write(String.valueOf(item));
                writer.newLine();
            }
            System.out.println("List written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static CopyOnWriteArrayList<Integer> readListFromFile(String filename) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(Integer.parseInt(line));
            }
            System.out.println("List read from file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    private static CopyOnWriteArrayList<Integer> updateFile(List<Integer> list, String filename) throws IOException {
        writeArrayToFile(list, filename);
        return readListFromFile(filename);
    }
    private static CopyOnWriteArrayList<Integer> mergeArrays(CopyOnWriteArrayList<Integer> array1, CopyOnWriteArrayList<Integer> array2, CopyOnWriteArrayList<Integer> array3) {
        Set<Integer> mergedSet = new TreeSet<>(array3);
        mergedSet.removeAll(array1);
        mergedSet.removeAll(array2);
        return new CopyOnWriteArrayList<>(mergedSet);
    }

}