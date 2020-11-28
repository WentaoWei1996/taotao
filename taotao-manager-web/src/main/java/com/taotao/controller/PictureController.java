package com.taotao.controller;

import com.taotao.managerweb.util.FastDFSClient;
import com.taotao.managerweb.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wwt
 * @date 2019/9/19 15:28
 */

@Controller
public class PictureController {
    @Value("${TAOTAO_IMAGE_SERVER_URL}")
    private String TAOTAO_IMAGE_SERVER_URL;// = "http://192.168.25.133/";

    /*@RequestMapping("/pic/upload")
    @ResponseBody
    public Map fileUpload(MultipartFile uploadFile) {
        try {
            //1、取文件的扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //2、创建一个FastDFS的客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
            //3、执行上传处理
            String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            //4、拼接返回的url和ip地址，拼装成完整的url
            String url = TAOTAO_IMAGE_SERVER_URL + path;
            //5、返回map
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            //5、返回map
            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return result;
        }
    }*/

    /**
     * 上传图片
     *
     * Content-Type:application/json;charset=UTF-8  responseBody默认的返回类型
     * 可以通过produces 设置返回类型
     返回JSON格式字符串时：
     Content-Type:text/plain;charset=UTF-8  火狐浏览器也支持

     * @param uploadFile
     * @return
     */
    @RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String uploadPic(MultipartFile uploadFile){
        try {
            //1.创建一个fastdfsclient对象
            FastDFSClient client = new FastDFSClient("classpath:fdfs_client.conf");
            //2.获取元文件的字节数组
            byte[] bytes = uploadFile.getBytes();
            //3.获取元文件的扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            //不带点的扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);

            //4.调用方法上传图片
            //返回的字符串：group1/M00/00/01/wKgZhVjnAd6AKj_RAAvqH_kipG8211.jpg
            String uploadFile2 = client.uploadFile(bytes, extName);

            System.out.println(TAOTAO_IMAGE_SERVER_URL+uploadFile2);

            //5.设置上传成功后的图片的路径和错误信息
            Map map = new HashMap<>();
            map.put("error", 0);
            map.put("url", TAOTAO_IMAGE_SERVER_URL+uploadFile2);
            //6.返回
            return JsonUtils.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            //不成功的情况
            Map map = new HashMap<>();
            map.put("error", 0);
            map.put("message","上传图片失败");
            return JsonUtils.objectToJson(map);
        }
    }

}
