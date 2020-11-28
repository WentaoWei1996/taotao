package com.taotao.test.fastdfs;

import com.taotao.managerweb.util.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.IOException;

/**
 * @author wwt
 * @date 2019/9/19 14:37
 */
public class FastDFSTest {

    @Test
    public void testFileUpload() throws IOException, MyException {
        ClientGlobal.init("/Users/weiwentao/IdeaProjects/taotao/taotao-manager-web/src/main/resources/fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        String[] strings = storageClient.upload_file("/Users/weiwentao/Downloads/wwt.jpg","jpg",null);
        for (String str : strings){
            System.out.println(str);
        }
    }

    @Test
    public void testFastDfsClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("/Users/weiwentao/IdeaProjects/taotao/taotao-manager-web/src/main/resources/fdfs_client.conf");
        String file = fastDFSClient.uploadFile("/Users/weiwentao/Downloads/wwt.jpg");
        System.out.println(file);
    }
}
