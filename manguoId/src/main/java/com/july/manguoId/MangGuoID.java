package com.july.manguoId;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by haoyifen on 2017/5/22 20:14.
 * 用于反解析生成的ids字节数组
 **/
public class MangGuoID {
    private byte[] bytes;
    private String ip;
    private int pid;
    private int random;
    private Date time;
    private int seq;

    public MangGuoID(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("length must be 16");
        }
        this.bytes = bytes;
        init();
    }

    public MangGuoID() {
        this.bytes = IDS.getIDS();
        init();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Stopwatch stopwatch = Stopwatch.createStarted();
            int size = 1000 * 100;
            Set<MangGuoID> collect = IntStream.range(0, size).parallel()
                    .mapToObj(it -> new MangGuoID())
                    .collect(Collectors.toSet());
            if (collect.size() != size) {
                throw new IllegalArgumentException("non identical");
            }
            //60ms
            System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    private void init() {
        this.ip = ip();
        this.pid = pid();
        this.random = randomInt();
        this.time = date();
        this.seq = seq();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MangGuoID myUUID = (MangGuoID) o;

        return Arrays.equals(bytes, myUUID.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    private String ip() {
        byte[] ipBytes = new byte[4];
        System.arraycopy(this.bytes, 0, ipBytes, 0, ipBytes.length);
        int ip = Ints.fromByteArray(ipBytes);
        return Net.intToStringIp(ip);
    }

    private int pid() {
        byte[] pidIntBytes = {0, 0, this.bytes[4], this.bytes[5]};
        int i = Ints.fromByteArray(pidIntBytes);
        return i;
    }

    private int randomInt() {
        byte[] randomIntBytes = {0, 0, this.bytes[6], this.bytes[7]};
        int i = Ints.fromByteArray(randomIntBytes);
        return i;
    }

    private Date date() {
        byte[] timeBytes = new byte[6];
        System.arraycopy(bytes, 8, timeBytes, 0, timeBytes.length);
        byte[] concat = Bytes.concat(new byte[]{0, 0}, timeBytes);
        long timeStamp = Longs.fromByteArray(concat);
        return new Date(timeStamp);
    }

    private int seq() {
        byte[] randomIntBytes = {0, 0, this.bytes[14], this.bytes[15]};
        int i = Ints.fromByteArray(randomIntBytes);
        return i;
    }

    @Override
    public String toString() {
        return "MyUUID{" +
                "ip='" + ip + '\'' +
                ", pid=" + pid +
                ", random=" + random +
                ", time=" + time +
                ", seq=" + seq +
                '}';
    }

}
