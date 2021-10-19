package com.pj.project4sp.article4search;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Async
    @Override
    public void indexFile(Long articleId, List<String> urls, Long userId, LocalDateTime time) {
        urls.forEach(url -> new Attachment(articleId, url, userId, time).updateESFile());
    }

    @Async
    @Override
    public void deleteFile(List<String> urls) {
        urls.forEach(url -> {
            MinioUtil.removeObject(url);
            EsUtil.deleteDocument("fileindex", Integer.toString(HashUtil.fnvHash(url)));
        });
    }

    @Override
    public List<AttachmentVo> search(String key) {

        SearchSourceBuilder builder = new SearchSourceBuilder();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("fileName").preTags("`").postTags("`")
                .field("attachment.content").preTags("`").postTags("`").fragmentSize(500).noMatchSize(50);

        //因为我这边实际业务需要其他字段的查询，所以进行查询的字段就比较，如果只是查询文档中内容的话，打开注释的代码，然后注释掉这行代码
        builder.query(QueryBuilders.multiMatchQuery(key,"attachment.content", "fileName").analyzer("ik_smart")).highlighter(highlightBuilder);

        //builder.query(QueryBuilders.matchQuery("attachment.content", key).analyzer("ik_smart"));
        SearchResponse searchResponse = EsUtil.selectDocument("fileindex", builder);
        SearchHits hits = searchResponse.getHits();
        if (CollUtil.isEmpty(Arrays.asList(hits.getHits()))) return new LinkedList<>();


        return Arrays.stream(hits.getHits()).map(hit -> {
            JSONUtil.parseObj(hit.getSourceAsString());
            AttachmentVo attachmentVo = JSONUtil.toBean(JSONUtil.parseObj(hit.getSourceAsString()), AttachmentVo.class);
            Object attachment = hit.getSourceAsMap().get("attachment");
            String content = JSONUtil.parseObj(attachment).get("content", String.class);
            attachmentVo.setContents(new LinkedList<>());
            attachmentVo.getContents().add(content);
            Text[] fileNames = hit.getHighlightFields().get("fileName").getFragments();
            if (CollUtil.isNotEmpty(Arrays.asList(fileNames))) {
                attachmentVo.setFileName(fileNames[0].string());
            }
            Text[] highlightContents = hit.getHighlightFields().get("attachment.content").getFragments();
            if (CollUtil.isNotEmpty(Arrays.asList(highlightContents))) {
                attachmentVo.setContents(Arrays.stream(highlightContents).map(Text::string).collect(Collectors.toList()));
            }
//            System.out.println(attachmentVo);
            return attachmentVo;
        }).collect(Collectors.toList());
    }

}
