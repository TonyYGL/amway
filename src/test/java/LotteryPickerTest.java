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

    // 模擬1000個抽獎行為同時執行
    private final int DRAWING_COUNT_AT_ONCE = 1000;

    // 玩家個數, 模擬重複抽獎行為
    private final int USER_COUNT = 900;

    @Test
    public void testDrawingGame() throws Exception {
        List<Prize> prizeList = Arrays.asList(
                new Prize("iPhone", 5.0, 2),
                new Prize("Mac book", 10.0, 5),
                new Prize("NT 20000", 30.0, 10)
        );
        LotteryPicker lotteryPicker = new LotteryPicker(prizeList);
        Map<String, AtomicInteger> resultMap = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(DRAWING_COUNT_AT_ONCE);

        CountDownLatch readyLatch = new CountDownLatch(DRAWING_COUNT_AT_ONCE);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(DRAWING_COUNT_AT_ONCE);

        for (int i = 0; i < DRAWING_COUNT_AT_ONCE; i++) {
            executor.submit(() -> {
                // 隨機產生userId, 模擬重複抽獎情境
                String userId = getRandomUserId(USER_COUNT);
                readyLatch.countDown(); // 表示這個 thread 準備好了
                try {
                    startLatch.await(); // 等待所有 thread 準備好再同時起跑
                    String result = lotteryPicker.draw(userId);
                    resultMap.computeIfAbsent(result, k -> new AtomicInteger(0)).incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
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

    private String getRandomUserId(int userCount) {
        int random = (int) (Math.random() * userCount) + 1;
        return "userId" + random;
    }
}
