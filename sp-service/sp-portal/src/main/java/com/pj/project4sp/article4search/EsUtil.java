package com.pj.project4sp.article4search;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class EsUtil {
    /**
     * 客户端,本次使用本地连接
     */
    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("elasticsearch", 9200, "http")));


    /**
     * 停止连接
     */
    public static void shutdown() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 默认类型(整个type类型传闻在8.0版本后可能会废弃,但是目前7.13版本下先保留)
     */
//    public static final String DEFAULT_TYPE = "_doc";

    /**
     * set方法前缀
     */
    public static final String SET_METHOD_PREFIX = "set";

    /**
     * 返回状态-CREATED
     */
    public static final String RESPONSE_STATUS_CREATED = "CREATED";

    /**
     * 返回状态-OK
     */
    public static final String RESPONSE_STATUS_OK = "OK";

    /**
     * 返回状态-NOT_FOUND
     */
    public static final String RESPONSE_STATUS_NOT_FOUND = "NOT_FOUND";

    /**
     * 需要过滤的文档数据
     */
    public static final String[] IGNORE_KEY = {"@timestamp", "@version", "type"};

    /**
     * 超时时间 1s
     */
    public static final TimeValue TIME_VALUE_SECONDS = TimeValue.timeValueSeconds(1);

    /**
     * 批量新增
     */
    public static final String PATCH_OP_TYPE_INSERT = "insert";

    /**
     * 批量删除
     */
    public static final String PATCH_OP_TYPE_DELETE = "delete";

    /**
     * 批量更新
     */
    public static final String PATCH_OP_TYPE_UPDATE = "update";

//==========================================数据操作(工具)(不参与调用es)=================================================

    /**
     * 方法描述: 剔除指定文档数据,减少不必要的循环
     *
     * @param map 文档数据
     * @return: void
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:39 上午
     */
    public static void ignoreSource(Map<String, Object> map) {
        for (String key : IGNORE_KEY) {
            map.remove(key);
        }
    }


    /**
     * 方法描述: 将文档数据转化为指定对象
     *
     * @param sourceAsMap 文档数据
     * @param clazz       转换目标Class对象
     * @return 对象
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:38 上午
     */
    public static <T> T dealObject(Map<String, Object> sourceAsMap, Class<T> clazz) {
        try {
            ignoreSource(sourceAsMap);
            Iterator<String> keyIterator = sourceAsMap.keySet().iterator();
            T t = clazz.newInstance();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                String replaceKey = key.replaceFirst(key.substring(0, 1), key.substring(0, 1).toUpperCase());
                Method method = null;
                try {
                    method = clazz.getMethod(SET_METHOD_PREFIX + replaceKey, sourceAsMap.get(key).getClass());
                } catch (NoSuchMethodException e) {
                    continue;
                }
                method.invoke(t, sourceAsMap.get(key));
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//==========================================索引操作=================================================

    /**
     * 方法描述: 创建索引,若索引不存在且创建成功,返回true,若同名索引已存在,返回false
     *
     * @param: [index] 索引名称
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 11:01 上午
     */
    public static boolean insertIndex(String index) {
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //执行创建请求IndicesClient,请求后获得响应
        try {
            CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
            return response != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 方法描述: 判断索引是否存在,若存在返回true,若不存在或出现问题返回false
     *
     * @param: [index] 索引名称
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 11:09 上午
     */
    public static boolean isExitsIndex(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        try {
            return client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
     * 方法描述: 删除索引,删除成功返回true,删除失败返回false
     * @param: [index] 索引名称
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 11:23 上午
     */
    public static boolean deleteIndex(String index) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        try {
            AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//==========================================文档操作(新增,删除,修改)=================================================

    /**
     * 方法描述: 新增/修改文档信息
     *
     * @param index 索引
     * @param id    文档id
     * @param data  数据
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:34 上午
     */
    public static boolean insertOrUpdateDocument(String index, String id, Object data) {
        try {
            IndexRequest request = new IndexRequest(index);
            request.timeout(TIME_VALUE_SECONDS);
            if (id != null && id.length() > 0) {
                request.id(id);
            }
            request.source(JSON.toJSONString(data), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            String status = response.status().toString();
            if (RESPONSE_STATUS_CREATED.equals(status) || RESPONSE_STATUS_OK.equals(status)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 方法描述: 更新文档信息
     *
     * @param index 索引
     * @param id    文档id
     * @param data  数据
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:34 上午
     */
    public static boolean updateDocument(String index, String id, Object data) {
        try {
            UpdateRequest request = new UpdateRequest(index, id);
            request.doc(JSON.toJSONString(data), XContentType.JSON);
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            String status = response.status().toString();
            if (RESPONSE_STATUS_OK.equals(status)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 方法描述:删除文档信息
     *
     * @param index 索引
     * @param id    文档id
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:33 上午
     */
    public static boolean deleteDocument(String index, String id) {
        try {
            DeleteRequest request = new DeleteRequest(index, id);
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            String status = response.status().toString();
            if (RESPONSE_STATUS_OK.equals(status)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 方法描述: 小数据量批量新增
     *
     * @param index    索引
     * @param dataList 数据集 新增修改需要传递
     * @param timeout  超时时间 单位为秒
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:31 上午
     */
    public static boolean simplePatchInsert(String index, List<Object> dataList, long timeout) {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout(TimeValue.timeValueSeconds(timeout));
            if (dataList != null && dataList.size() > 0) {
                for (Object obj : dataList) {
                    bulkRequest.add(
                            new IndexRequest(index)
                                    .source(JSON.toJSONString(obj), XContentType.JSON)
                    );
                }
                BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                if (!response.hasFailures()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 功能描述:
     * @param index 索引名称
     * @param idList 需要批量删除的id集合
     * @return : boolean
     * @author : gxf
     * @date : 2021/6/30 1:22
     */
    public static boolean patchDelete(String index, List<String> idList) {
        BulkRequest request = new BulkRequest();
        for (String id:idList) {
            request.add(new DeleteRequest().index(index).id(id));
        }
        try {
            BulkResponse response = EsUtil.client.bulk(request, RequestOptions.DEFAULT);
            return !response.hasFailures();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//==========================================文档操作(查询)=================================================

//    /**
//     * 方法描述: 判断文档是否存在
//     *
//     * @param index 索引
//     * @param id    文档id
//     * @return: boolean
//     * @author: gxf
//     * @date: 2021年07月27日
//     * @time: 10:36 上午
//     */
//    public static boolean isExistsDocument(String index, String id) {
//        return isExistsDocument(index, id);
//    }


    /**
     * 方法描述: 判断文档是否存在
     *
     * @param index 索引
//     * @param type  类型
     * @param id    文档id
     * @return: boolean
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:36 上午
     */
    public static boolean isExistsDocument(String index, String id) {
        GetRequest request = new GetRequest(index, id);
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            return response.isExists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    /**
//     * 方法描述: 根据id查询文档
//     *
//     * @param index 索引
//     * @param id    文档id
//     * @param clazz 转换目标Class对象
//     * @return 对象
//     * @author: gxf
//     * @date: 2021年07月27日
//     * @time: 10:36 上午
//     */
//    public static <T> T selectDocumentById(String index, String id, Class<T> clazz) {
//        return selectDocumentById(index, DEFAULT_TYPE, id, clazz);
//    }


    /**
     * 方法描述: 根据id查询文档
     *
     * @param index 索引
//     * @param type  类型
     * @param id    文档id
     * @param clazz 转换目标Class对象
     * @return 对象
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:35 上午
     */
    public static <T> T selectDocumentById(String index, String id, Class<T> clazz) {
        try {
//            type = type == null || type.equals("") ? DEFAULT_TYPE : type;

            GetRequest request = new GetRequest(index, id);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            if (response.isExists()) {
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                return dealObject(sourceAsMap, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述:（筛选条件）获取数据集合
     *
     * @param index         索引
     * @param sourceBuilder 请求条件
     * @param clazz         转换目标Class对象
     * @return: java.util.List<T>
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:35 上午
     */
    public static <T> List<T> selectDocumentList(String index, SearchSourceBuilder sourceBuilder, Class<T> clazz) {
        try {
            SearchRequest request = new SearchRequest(index);
            if (sourceBuilder != null) {
                // 返回实际命中数
                sourceBuilder.trackTotalHits(true);
                request.source(sourceBuilder);
            }
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                SearchHit[] hits = response.getHits().getHits();
                for (SearchHit documentFields : hits) {
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    list.add(dealObject(sourceAsMap, clazz));
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述:（筛选条件）获取数据
     *
     * @param index         索引
     * @param sourceBuilder 请求条
     * @return: java.util.List<T>
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 10:35 上午
     */
    public static SearchResponse selectDocument(String index, SearchSourceBuilder sourceBuilder) {
        try {
            SearchRequest request = new SearchRequest(index);
            if (sourceBuilder != null) {
                // 返回实际命中数
                sourceBuilder.trackTotalHits(true);
                sourceBuilder.size(10000);
                request.source(sourceBuilder);
            }
            return client.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述: 筛选查询,返回使用了<span style='color:red'></span>处理好的数据.
     *
     * @param: index 索引名称
     * @param: sourceBuilder sourceBuilder对象
     * @param: clazz 需要返回的对象类型.class
     * @param: highLight 需要表现的高亮匹配字段
     * @return: java.util.List<T>
     * @author: gxf
     * @date: 2021年07月27日
     * @time: 6:39 下午
     */
    public static <T> List<T> selectDocumentListHighLight(String index, SearchSourceBuilder sourceBuilder, Class<T> clazz, String highLight) {
        try {
            SearchRequest request = new SearchRequest(index);
            if (sourceBuilder != null) {
                // 返回实际命中数
                sourceBuilder.trackTotalHits(true);
                //高亮
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field(highLight);
                highlightBuilder.requireFieldMatch(false);//多个高亮关闭
                highlightBuilder.preTags("<span style='color:red'>");
                highlightBuilder.postTags("</span>");
                sourceBuilder.highlighter(highlightBuilder);
                request.source(sourceBuilder);
            }
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                for (SearchHit documentFields : response.getHits().getHits()) {
                    Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                    HighlightField title = highlightFields.get(highLight);
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    if (title != null) {
                        Text[] fragments = title.fragments();
                        String n_title = "";
                        for (Text fragment : fragments) {
                            n_title += fragment;
                        }
                        sourceAsMap.put(highLight, n_title);//高亮替换原来的内容
                    }
                    list.add(dealObject(sourceAsMap, clazz));
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法描述: 返回索引内所有内容,返回SearchResponse对象,需要自己解析,不对数据封装
     * @param: index 索引名称
     * @return: SearchResponse
     * @author: gxf
     * @date: 2021/6/30
     * @time: 1:28 上午
     */
    public static SearchResponse queryAllData(String index){
        //创建搜索请求对象
        SearchRequest request = new SearchRequest(index);
        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询所有数据
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(sourceBuilder);
        try {
            return client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述: 返回索引内所有内容,返回指定类型
     * @param: index 索引名称
     * @param: clazz 需要接受转换的对象类型
     * @return: java.util.List<T>
     * @author: gxf
     * @date: 2021/6/30
     * @time: 1:32 上午
     */
    public static <T> List<T> queryAllData(String index, Class<T> clazz){
        //创建搜索请求对象
        SearchRequest request = new SearchRequest(index);
        //构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询所有数据
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(sourceBuilder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response.getHits() != null) {
                List<T> list = new ArrayList<>();
                SearchHit[] hits = response.getHits().getHits();
                for (SearchHit documentFields : hits) {
                    Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
                    list.add(dealObject(sourceAsMap, clazz));
                }
                return list;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
