package com.greatmap.digital.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间戳(毫秒级)，注意，41位时间戳不是存储当前时间的时间戳，而是存储时间戳的差值（当前时间戳 - 开始时间戳)
 * 得到的值），这里的的开始时间戳，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下面程序SnowFlakeIdWorker类的twepoch属性）。
 * 8位的数据机器位，可以部署在256个节点，包括4位datacenterId和4位workerId
 * 8位序列，毫秒内的计数，8位的计数顺序号支持每个节点每毫秒(同一机器，同一时间戳)产生256个ID序号
 *
 * @author guoan
 */
public class SnowFlakeIdWorker {

    /**
     * 开始时间戳 (2019-01-01)
     */
    private final long twepoch = 1546272040523L;

    /**
     * 机器ID所占的位数
     */
    private final long workerIdBits = 4L;

    /**
     * 数据标识ID所占的位数
     */
    private final long dataCenterIdBits = 4L;

    /**
     * 支持的最大机器ID，结果是15 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是15
     */
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 序列在id中占的位数，目前设置为8位
     */
    private final long sequenceBits = 8L;

    /**
     * 机器ID向左移8位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移12位(8+4)
     */
    private final long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移16位(4+4+8)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为255
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~15)
     */
    private long workerId;

    /**
     * 数据中心ID(0~15)
     */
    private long dataCenterId;

    /**
     * 毫秒内序列(0~255)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    public SnowFlakeIdWorker(){

    }

    /**
     * 构造函数
     * @param workerId 工作ID (0~15)
     * @param dataCenterId 数据中心ID (0~15)
     */
    public SnowFlakeIdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowFlakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException( String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0L;
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    /** 测试 */
    public static void main(String[] args) {
        SnowFlakeIdWorker idWorker = new SnowFlakeIdWorker(1, 1);
        Set<Long> longSet = new HashSet<>();
        Long n = 1000000L;
        for (int i = 0; i < n; i++) {
            long id = idWorker.nextId();
            longSet.add(id);
        }
        System.out.println(longSet.size());
    }
}

