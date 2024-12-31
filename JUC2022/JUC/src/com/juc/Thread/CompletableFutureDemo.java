package com.juc.Thread;

import java.util.concurrent.*;

public class CompletableFutureDemo {

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100));

    public static void main(String[] args) {
        findCustomer();
        findCustomerByCompletableFuture();
    }

    // 同步进行
    public static void findCustomer() {
        long startTime = System.currentTimeMillis();
        getCustomerName();
        getScore();
        getOrderInfo();
        long endTime = System.currentTimeMillis();
        System.out.println("----costTime: " + (endTime - startTime) + "毫秒");
    }

    // 数据异步进行
    public static void findCustomerByCompletableFuture() {
        long startTime = System.currentTimeMillis();

        CompletableFuture<Void> customerInfoFuture = CompletableFuture.runAsync(() -> {
            getCustomerName();
        }, threadPool);

        CompletableFuture<Void> scoreFuture = CompletableFuture.runAsync(() -> {
            getScore();
        }, threadPool);

        CompletableFuture<Void> orderInfo = CompletableFuture.runAsync(() -> {
            getOrderInfo();
        }, threadPool);

        CompletableFuture.allOf(customerInfoFuture, scoreFuture, orderInfo).join();

        long endTime = System.currentTimeMillis();
        System.out.println("异步调用：----costTime: " + (endTime - startTime) + "毫秒");

    }

    // 模拟三个不同的服务耗时
    public static String getCustomerName() {
        try { TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e) {throw new RuntimeException(e);}
        return "张三";
    }

    public static Long getScore() {
        try { TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {throw new RuntimeException(e);}
        return 100L;
    }

    public static String getOrderInfo() {
        try { TimeUnit.MILLISECONDS.sleep(300);} catch (InterruptedException e) {throw new RuntimeException(e);}
        return "OrderSerial01-iphone";
    }
}
