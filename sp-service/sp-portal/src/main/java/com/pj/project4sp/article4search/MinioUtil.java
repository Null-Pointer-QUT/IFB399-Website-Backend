package com.pj.project4sp.article4search;

import cn.hutool.core.text.StrSplitter;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MinioUtil {

    private static MinioClient minioClient;

    static {
        try {
            minioClient = new MinioClient("https://minio.juntao.life:443", "HALOCAMPUSMINIO", "HalocampusMiniO");
        } catch (InvalidEndpointException | InvalidPortException ignored) {}
    }

    public static void removeObject(String url) {
        try {
            List<String> urlList = StrSplitter.split(url, '/', 0, true, true);
            String fileName = "test/" + urlList.get(urlList.size() - 1);
            // 从mybucket中删除myobject。
            minioClient.removeObject("ifb399", fileName);
            System.out.println("successfully removed ifb399/" + fileName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException ignored) {}
    }

}
