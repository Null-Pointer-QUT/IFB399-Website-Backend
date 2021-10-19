package com.pj;

import com.pj.project4sp.article4search.MinioUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MinioTest {

    @Test
    public void testRemove() {
        MinioUtil.removeObject("https://minio.juntao.life/ifb399/test/1633776420523_Week 10 IAB206 Lecture 10 STREAMING DATA-Part2.pdf");
    }
}
