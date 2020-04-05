package com.xuecheng.test.fastdfs;

import org.csource.fastdfs.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    @Test
    public void testUpload() {
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackClient，用于请求TrackerServer
            TrackerClient client = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = client.getConnection();
            //获取Storage
            StorageServer storeStorage = client.getStoreStorage(trackerServer);
            //创建storageClient
            //StorageClient storageClient = new StorageClient1(trackerServer,storeStorage);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //向storageClient上传文件
            //本地文件路径
            String filepath = "E:\\upload\\image\\1.jpg";
            String fileId = storageClient1.upload_file1(filepath, "jpg", null);
            System.out.println(fileId);
            //group1/M00/00/01/wKgZhV54h4iAMfWlAAPChaVwjko530.jpg

            // 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDownload(){
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackClient，用于请求TrackerServer
            TrackerClient client = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = client.getConnection();
            //获取Storage
            StorageServer storeStorage = client.getStoreStorage(trackerServer);
            //创建storageClient
            //StorageClient storageClient = new StorageClient1(trackerServer,storeStorage);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //下载文件
            byte[] bytes = storageClient1.download_file1("group1/M00/00/01/wKgZhV54h4iAMfWlAAPChaVwjko530.jpg");
            //使用输出流保存文件
            FileOutputStream fileOutputStream = new FileOutputStream(new File("F:\\tp\\2.jpg"));
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
