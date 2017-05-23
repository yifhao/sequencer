package com.july.manguoId;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.lang.management.ManagementFactory;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by haoyifen on 2017/5/22 16:41.
 */
public class IDS {
    private final static int ip = Net.stringIpToInt(Net.firstNon127Or192());
    private final static SecureRandom SECURE_RANDOM = new SecureRandom();
    private final static int pid = pid();
    private final static int randomInt = SECURE_RANDOM.nextInt(Integer.MAX_VALUE);
    private static AtomicInteger seq = new AtomicInteger(SECURE_RANDOM.nextInt(Integer.MAX_VALUE));

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            byte[] ids = getIDS();
            System.out.println(Arrays.toString(ids));
        }
    }

    private static int pid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
// get pid
        int index = name.indexOf("@");
        if (index == -1) {
            return SECURE_RANDOM.nextInt(65535);
        }
        int pid = Integer.parseInt(name.substring(0, index));
        return pid;
    }

    public static byte[] getIDS() {
        byte[] ipBytes = Ints.toByteArray(ip);
        //pid使用低两个字节. 其本身也就只有
        byte[] pidBytes = leastTwoBytes(pid);
        byte[] randomIntBytes = leastTwoBytes(randomInt);
        long mills = System.currentTimeMillis();
        //时间使用ms, 使用低6个字节,48位, 可以使用8925年
        byte[] timeBytes = leastSixBytes(mills);
        int seq = IDS.seq.getAndIncrement();
        byte[] seqBytes = leastTwoBytes(seq);
        byte[] uuid = Bytes.concat(ipBytes, pidBytes, randomIntBytes, timeBytes, seqBytes);
        return uuid;
    }

    private static byte[] leastSixBytes(long mills) {
        byte[] millsBytes = Longs.toByteArray(mills);
        byte[] timeBytes = new byte[6];
        System.arraycopy(millsBytes, 2, timeBytes, 0, 6);
        return timeBytes;
    }

    private static byte[] leastTwoBytes(int pid) {
        byte[] bytes = Ints.toByteArray(pid);
        byte highByte = bytes[2];
        byte lowByte = bytes[3];
        return new byte[]{highByte, lowByte};
    }
}
