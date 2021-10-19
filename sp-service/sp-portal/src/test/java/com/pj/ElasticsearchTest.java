package com.pj;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.json.JSONUtil;
import com.pj.project4sp.article4search.Attachment;
import com.pj.project4sp.article4search.AttachmentVo;
import com.pj.project4sp.article4search.EsUtil;
import com.pj.project4sp.article4search.IAttachmentService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class ElasticsearchTest {

    @Resource
    private IAttachmentService attachmentService;

    @Test
    public void uploadTest() {
        Attachment attachment = new Attachment(1445467239952945152L, "https://minio.juntao.life:443/ifb399/test/1633462006500_IAB206- Lec8- Blockchain Applications_extract.pdf", 20012L, LocalDateTime.now());
        System.out.println(attachment);
        attachment.updateESFile();
    }

    @Test
    public void searchTest1() {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("fileName").preTags("<mark>").postTags("</mark>")
                .field("attachment.content").preTags("<mark>").postTags("</mark>").noMatchSize(50);

        //因为我这边实际业务需要其他字段的查询，所以进行查询的字段就比较，如果只是查询文档中内容的话，打开注释的代码，然后注释掉这行代码
        builder.query(QueryBuilders.multiMatchQuery("We discussed the basics of blockchain!","attachment.content", "fileName" ,"userId").analyzer("ik_smart")).highlighter(highlightBuilder);

        //builder.query(QueryBuilders.matchQuery("attachment.content", key).analyzer("ik_smart"));
        SearchResponse searchResponse = EsUtil.selectDocument("fileindex", builder);
        SearchHits hits = searchResponse.getHits();


        Arrays.stream(hits.getHits()).map(hit -> {
//            System.out.println(hit.getSourceAsMap());
//            System.out.println(hit.getSourceAsString());
            JSONUtil.parseObj(hit.getSourceAsString());
            AttachmentVo attachmentVo = JSONUtil.toBean(JSONUtil.parseObj(hit.getSourceAsString()), AttachmentVo.class);
            System.out.println(attachmentVo);
//            AttachmentVo attachmentVo = EsUtil.dealObject(hit.getSourceAsMap(), AttachmentVo.class);
//            Object attachment = hit.getSourceAsMap().get("attachment");
//            String content = JSONUtil.parseObj(attachment).get("content", String.class);
//            attachmentVo.setContent(content);
//            Text[] fileNames = hit.getHighlightFields().get("fileName").getFragments();
//            if (CollUtil.isNotEmpty(Arrays.asList(fileNames))) {
//                attachmentVo.setFileName(fileNames[0].string());
//            }
//            Text[] highlightContents = hit.getHighlightFields().get("attachment.content").getFragments();
//            if (CollUtil.isNotEmpty(Arrays.asList(highlightContents))) {
//                attachmentVo.setContent(Arrays.stream(highlightContents).map(Text::string).collect(Collectors.joining("  ")));
//            }
            return attachmentVo;
        }).collect(Collectors.toList());
    }

    @Test
    public void searchTest() {
        List<AttachmentVo> attachmentVos = attachmentService.search("We discussed the basics of blockchain!");
        System.out.println(attachmentVos);
        List<AttachmentVo> attachmentVos1 = attachmentService.search("Lec8");
        System.out.println(attachmentVos1);
//        System.out.println(result);
    }

    @Test
    public void testHash() {
        int hash = HashUtil.fnvHash("asjdkfa  asdjfa osdjfpa sdf aosd faj sdf asdf");
        System.out.println(hash);
        hash = HashUtil.fnvHash("asjdkfa  asdjfa osdjfpa sdf aosd faj sdf asd1");
        System.out.println(hash);
    }

    @Test
    public void testDelete() {
        EsUtil.deleteDocument("fileindex", "l8HvUXwBPFOBo0u-S_me");
    }
}
