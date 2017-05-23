package com.july.leafClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.july.leafClient.util.RESTUtil;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by haoyifen on 2017/5/23 12:39.
 */
public class LeafSequencer implements Sequencer {
    private final String bizTag;
    private final ExecutorService executor = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });
    private volatile Map<String, Object> currentRange;
    private volatile Map<String, Object> nextRange;
    private volatile long currentSeq;
    private volatile Future<Map<String, Object>> loadFuture;

    public LeafSequencer(String bizTag) {
        this.bizTag = bizTag;
    }

    @Override
    public long nextSeq() {
        synchronized (this) {
            //如果currentRange为null, 就表示第一次加载
            if (currentRange == null) {
                currentRange = getNextRange();
                currentSeq = currentMinId();
                return currentSeq;
            }
            long step = currentMaxId() - currentMinId();
            //如果超过了20%的已经使用了, 那么启动一个线程加载nextRange
            boolean overLoadThreshold = (currentSeq - currentMinId()) * 1.0 / step >= 0.2;
            //切换range之后, 没有加载过
            boolean loadingNotStart = (loadFuture == null);

            if (overLoadThreshold && loadingNotStart) {
                loadFuture = executor.submit(this::getNextRange);
            }

            //当前range已经使用完了
            if (currentSeq >= currentMaxId() - 1) {
                //一般来说这里是不需要等待的, 因为在使用到20%的时候已经加载下一个了.
                try {
                    nextRange = loadFuture.get(100, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("load next range error: " + e.getMessage());
                }
                return switchAndReturn();
            }
            return ++currentSeq;
        }
    }

    private long switchAndReturn() {
        currentRange = nextRange;
        nextRange = null;
        loadFuture = null;
        currentSeq = currentMinId();
        return currentSeq;
    }


    private long currentMaxId() {
        return (long) currentRange.get("max_id");
    }

    private long currentMinId() {
        return (long) currentRange.get("min_id");
    }

    private Map<String, Object> getNextRange() {
        Map<String, Object> post = RESTUtil.post("http://localhost:8080/ids?bizTag=" + bizTag, Collections.emptyMap(), new TypeReference<Map<String, Object>>() {
        });
        return post;
    }

    @Override
    public String toString() {
        return currentRange.toString();
    }
}
