package com.interview.vkras.utils;

public class BookScripts {
    public static final String GET_TOP10_SQL = "SELECT author, string_agg(LOWER(title), '') " +
            "as titles from book\n" +
            "where LOWER(title) like ? \n" +
            "group by author";
    public static final String FIND_TITLES_AMD_AUTHORS_SQL = "SELECT title, author FROM BOOK";

    public static final String FIND_ALL_SQL = "SELECT * FROM BOOK";

    public static final String INSERT_SQL = "INSERT INTO BOOK (title, author, description) VALUES (?, ?, ?)";
}
