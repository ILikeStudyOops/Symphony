package org.csq.util;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
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
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class EsUtil {
    public static void main(String[] args) {
        //创建es客户端
        RestHighLevelClient client = getClient("localhost", 9200, "http");

        //创建索引
        boolean created = createIndex(client, "spring");
        System.out.println(created);

        //查询索引信息
        System.out.println(getAliases(client, "spring"));
        System.out.println(getMappings(client, "spring"));
        System.out.println(getSettings(client, "spring"));

        //删除索引
        System.out.println(deleteIndex(client,"spring"));

        //关闭es连接
        boolean close = close(client);
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
}
