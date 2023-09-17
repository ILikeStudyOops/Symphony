package org.csq.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.csq.entity.EsTestEntity;
import org.elasticsearch.action.DocWriteResponse;
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
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EsUtil {
    public static void main(String[] args) {
        //创建es客户端
        RestHighLevelClient client = getClient("localhost", 9200, "http");

        //创建索引
        /*boolean created = createIndex(client, "spring");
        System.out.println(created);*/

        //查询索引信息
        System.out.println(getAliases(client, "spring"));
        System.out.println(getMappings(client, "spring"));
        System.out.println(getSettings(client, "spring"));

        //插入数据
        DocWriteResponse.Result result =
                insertData(client, "spring", "1003",
                        new EsTestEntity("test", "女", "13933333333"));

        //批量插入数据
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("spring").id("1004").source(XContentType.JSON,"name","zhangsan","sex","男","tel","18111111111"));
        bulkRequest.add(new IndexRequest().index("spring").id("1005").source(XContentType.JSON,"name","lisi","sex","女","tel","18122222222"));
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //更新数据
        Map<String,String> updateData = new HashMap<>();
        updateData.put("name","rose");
        updateData.put("sex","女");
        updateData.put("tel","13922222222");
        GetResult res = updateData(client, "spring", "1002", updateData);

        //查询数据
        GetResponse getResponse = getData(client,"spring","1001");
        System.out.println(getResponse.getSourceAsString());

        //全量查询
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("spring");
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHits hits = searchResponse.getHits();
        System.out.println("数据条数：" + hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        //删除数据
        DeleteResponse deleteResponse = deleteData(client,"spring","1003");
        deleteResponse.toString();

        //批量删除数据
        BulkRequest bulkRequest2 = new BulkRequest();
        bulkRequest2.add(new DeleteRequest().index("spring").id("1004"));
        bulkRequest2.add(new DeleteRequest().index("spring").id("1005"));
        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest2, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //删除索引
        //System.out.println(deleteIndex(client,"spring"));

        //关闭es连接
        boolean close = close(client);
    }

    /**
     * 删除数据
     * @param client
     * @param indexName
     * @param id
     * @return
     */
    private static DeleteResponse deleteData(RestHighLevelClient client, String indexName, String id) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index(indexName).id(id);
        try {
            return client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据id获取数据
     * @param client
     * @param indexName
     * @param id
     * @return
     */
    private static GetResponse getData(RestHighLevelClient client, String indexName, String id) {
        GetRequest getRequest = new GetRequest();
        getRequest.index(indexName).id(id);
        try {
            return client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取es客户端
     * @param host es服务主机ip
     * @param port es服务端口号
     * @param protocol 通讯协议，默认为http
     * @return
     */
    public static RestHighLevelClient getClient(String host,Integer port,String protocol){
        if (ObjectUtils.isEmpty(protocol)){
            protocol = "http";
        }
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host,port,protocol))
        );
        return esClient;
    }

    /**
     * 关闭es客户端连接
     * @param client
     * @return
     */
    public static boolean close(RestHighLevelClient client){
        try {
            client.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建索引
     * @param client es连接客户端对象
     * @param indexName 创建索引名称
     * @return
     */
    public static boolean createIndex(RestHighLevelClient client,String indexName){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return createIndexResponse.isAcknowledged();
    }

    public static GetIndexResponse getIndexResponse(RestHighLevelClient client,String indexName){
        GetIndexRequest getIndexRequest = new GetIndexRequest("spring");
        try {
            return client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, List<AliasMetadata>> getAliases(RestHighLevelClient client, String indexName){
        GetIndexResponse indecxResponse = getIndexResponse(client, indexName);
        return indecxResponse.getAliases();
    }

    public static Map<String, MappingMetadata> getMappings(RestHighLevelClient client, String indexName){
        GetIndexResponse indecxResponse = getIndexResponse(client, indexName);
        return indecxResponse.getMappings();
    }

    public static Map<String, Settings> getSettings(RestHighLevelClient client, String indexName){
        GetIndexResponse indecxResponse = getIndexResponse(client, indexName);
        return indecxResponse.getSettings();
    }

    public static boolean deleteIndex(RestHighLevelClient client,String indexName){
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("spring");
        try {
            AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return delete.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 向索引中插入数据
     * @param client
     * @param indexName
     * @param dataId
     * @param data
     * @return
     * @param <T>
     */
    public static <T> DocWriteResponse.Result insertData(RestHighLevelClient client,String indexName,String dataId,T data){
        //准备请求对象
        IndexRequest indexRequest = new IndexRequest();
        //指定插入数据的索引名和数据id
        indexRequest.index(indexName).id(dataId);
        //转换数据对象为Json字符串
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        indexRequest.source(json, XContentType.JSON);
        try {
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            return response.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 局部修改索引中的数据，原方法中可指定多个字段覆盖，该方法封装中最多只支持三个，多余三个需要自己实现
     * @param client
     * @param indexName
     * @param dataId
     * @param updateData
     * @return
     */
    public static GetResult updateData(RestHighLevelClient client, String indexName, String dataId, Map updateData){
        Object[] keys = updateData.keySet().toArray();
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName).id(dataId);
        switch (keys.length){
            case 1:updateRequest.doc(XContentType.JSON,keys[0],updateData.get(keys[0]));
            break;
            case 2:updateRequest.doc(XContentType.JSON,keys[0],updateData.get(keys[0]),
                    keys[1],updateData.get(keys[1]));
            break;
            case 3:updateRequest.doc(XContentType.JSON,keys[0],updateData.get(keys[0]),
                    keys[1],updateData.get(keys[1]),
                    keys[2],updateData.get(keys[2]));
            break;
            default:return null;
        }
        try {
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
            return updateResponse.getGetResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
