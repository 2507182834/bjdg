package com.test.dao;

import org.apache.ibatis.annotations.Param;

public interface testDao {
    public int addStu(@Param("name") String name);
}
