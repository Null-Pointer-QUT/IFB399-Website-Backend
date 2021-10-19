package com.pj.project4sp.article4search;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Data
@Slf4j
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] types = {"pdf", "doc", "docx", "rtf", "txt", "xls", "xlsx", "ppt", "pptx"};

    private String fileId;

    private Long articleId;

    private String fileName;

    private String url;

    private Long userId;

    private Long uploadTime;

    private String type;

    private String content;

    public Attachment(){}

    public Attachment(Long articleId, String url, Long userId, LocalDateTime time) {
        fileId = Integer.toString(HashUtil.fnvHash(url));
        this.articleId = articleId;
        this.url = url;
        List<String> urlList = StrSplitter.split(url, '/', 0, true, true);
        fileName = ReUtil.delFirst("^\\d+_", urlList.get(urlList.size() - 1));
        this.userId = userId;
        this.uploadTime = time.toEpochSecond(ZoneOffset.ofHours(8));
        List<String> fileNameList = StrSplitter.split(fileName, ".", 0, true, true);
        type = fileNameList.get(fileNameList.size() - 1);
    }

    public void updateESFile() {
        if (!Arrays.asList(types).contains(type)) return;

        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            InputStream inStream = conn.getInputStream();
            byte[] bytes = IoUtil.readBytes(inStream, true);
//            byte[] bytes = getContent(file);
            content = Base64.getEncoder().encodeToString(bytes);
            IndexRequest indexRequest = new IndexRequest("fileindex");
            //上传同时，使用attachment pipline进行提取文件
            indexRequest.id(this.getFileId());
            indexRequest.source(JSONUtil.toJsonStr(this), XContentType.JSON);
            indexRequest.setPipeline("attachment");
            IndexResponse indexResponse = EsUtil.client.index(indexRequest, RequestOptions.DEFAULT);
            log.info("send to eSearch:" + fileName);
            log.info("send to eSeach results:" + indexResponse);
        } catch (IOException ignored){}//| SAXException | TikaException e) { }
    }

    private byte[] getContent(File file) throws IOException {

        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

}
