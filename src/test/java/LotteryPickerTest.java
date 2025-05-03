import org.example.drawingGame.LotteryPicker;
import org.example.drawingGame.Prize;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LotteryPickerTest {

    @Test
    public void testDraw() throws Exception {
        List<Prize> prizeList = Arrays.asList(
                new Prize("iPhone", 5.0, 2),
                new Prize("Mac book", 10.0, 5),
                new Prize("NT 20000", 30.0, 20)
        );
        LotteryPicker lotteryPicker = new LotteryPicker(prizeList);
        Map<String, AtomicInteger> resultMap = new ConcurrentHashMap<>();

        int threadCount = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                readyLatch.countDown(); // 表示這個 thread 準備好了
                try {
                    startLatch.await(); // 等待所有 thread 準備好再同時起跑
                    // 抽獎邏輯
                    String result = lotteryPicker.draw();
                    resultMap.computeIfAbsent(result, k -> new AtomicInteger(0)).incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown(); // 表示這個 thread 結束
                }
            });
        }

        readyLatch.await(); // 等待所有 thread 準備好
        startLatch.countDown(); // 所有 thread 同時起跑
        doneLatch.await(); // 等待所有 thread 結束
        executor.shutdown();

        System.out.println("抽獎結果統計：");
        resultMap.forEach((prize, count) -> System.out.println(prize + ": " + count.get()));
    }
}
