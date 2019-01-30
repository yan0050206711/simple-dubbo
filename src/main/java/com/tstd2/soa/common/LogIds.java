package com.tstd2.soa.common;

import java.util.concurrent.ThreadLocalRandom;

public class LogIds {

    /**
     * 生成64bit的logId。
     *
     * @return logId。
     */
    public static long generate() {
        /*
         * 生成规则为：
         * |-- 1bit --|--   35bit   --|-- 28bit --|
         *      0      当前时间毫秒数   系统随机数
         */
        long time = System.currentTimeMillis();
        long rand = ThreadLocalRandom.current().nextLong();
        return ((time & 0x7ffffffffL) << 28) | (rand & 0xfffffff);
    }

    private LogIds() {
    }

}
