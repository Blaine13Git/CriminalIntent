package com.bignerdranch.android.criminalintent.database;

/**
 * 定义数据库的Schema（概要）
 * 结构如下：（使用内部类构造结构）
 * 数据库--
 *   表--
 *     表名--
 *     列--
 *
 */
public class CrimeDbSchema {

    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
