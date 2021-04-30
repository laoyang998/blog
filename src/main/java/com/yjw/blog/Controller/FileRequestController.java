package com.yjw.blog.Controller;

import com.yjw.blog.Util.FileUtil;
import com.yjw.blog.Util.IpUtil;
import com.yjw.blog.Util.JwtUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileRequestController {


    @RequestMapping(value="/file/download")
    public void download(@RequestParam("userName") String userName,
                         @RequestParam("fileName") String fileName)throws IOException{
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=requestAttributes.getResponse();
        //设置信息给客户端
        String type=new MimetypesFileTypeMap().getContentType(fileName);
        response.setHeader("Content-type",type);
        String hehe = new String(fileName.getBytes("utf-8"), "iso-8859-1");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        String fullFileName="/upload/files/"+userName+"/"+fileName;
        FileUtil.download(fullFileName, response);
    }

    @CrossOrigin
    @PostMapping("/file/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                           HttpServletRequest request) throws Exception {
        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);
        if (userid != null) {
            String uploadPath = "/upload/files/" + userid;
            String filename = multipartFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String s[] = filename.split("\\.");
            String randomFileName = uuid.toString() + "." + s[1];
            InputStream inputStream = multipartFile.getInputStream();
            File file = new File(uploadPath, randomFileName);
            FileUtils.copyInputStreamToFile(inputStream, file);
            inputStream.close();
            // 返回图片访问路径
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort()
                    +"/file/download"
                    +"?fileName="+ randomFileName
                    +"&&userName="+userid;

            Map<String,Object> mapData=new HashMap<String,Object>();
            mapData.put("url",url);
            Map<String,Object> mapRes=new HashMap<String,Object>();
            mapRes.put("errno",0);
            mapRes.put("data",mapData);
            return mapRes;
        }else{
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("errno",1);   //返回错误码
            map.put("data",null);
            return map;
        }
    }

    @CrossOrigin
    @PostMapping("/upload_video")
    public Map<String, Object> uploadVideo(@RequestParam("video") MultipartFile multipartFile,
                                           HttpServletRequest request) throws Exception {
        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);
        if (userid != null) {
            String uploadPath = "/upload/video/" + userid;
            String filename = multipartFile.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String s[] = filename.split("\\.");
            String randomFileName = uuid.toString() + "." + s[1];
            InputStream inputStream = multipartFile.getInputStream();
            File file = new File(uploadPath, randomFileName);
            FileUtils.copyInputStreamToFile(inputStream, file);
            inputStream.close();
            // 返回图片访问路径
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort()
                    +"/video/" +userid+"/"+ randomFileName;

            Map<String,Object> mapData=new HashMap<String,Object>();
            mapData.put("url",url);
            Map<String,Object> mapRes=new HashMap<String,Object>();
            mapRes.put("errno",0);
            mapRes.put("data",mapData);
            return mapRes;
        }else{
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("errno",1);   //返回错误码
            map.put("data",null);
            return map;
        }
    }

    @GetMapping("/video/{folder}/{filename}")
    public ResponseEntity<StreamingResponseBody> getVideo(@PathVariable("folder") String folder,
           @PathVariable("filename") String fileName) throws Exception {
        File file = new File("/upload/video/"+folder+"/"+fileName);
        if (!file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        StreamingResponseBody stream = out -> {
            try {
                final InputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) >= 0) {
                    out.write(bytes, 0, length);
                }
                inputStream.close();
                out.flush();
            } catch (final Exception e) {

            }
        };

        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4");
        return ResponseEntity.ok().headers(headers).body(stream);
    }

}
