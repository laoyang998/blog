package com.yjw.blog.Controller;

import com.yjw.blog.Util.IpUtil;
import com.yjw.blog.Util.JwtUtil;
import com.yjw.blog.VO.WangEditImg;
import com.yjw.blog.VO.WangImgResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ImgController {

    @CrossOrigin
    @RequestMapping("/uploadimg")
    public WangImgResponse uploadImg(@RequestParam("myFile") MultipartFile multipartFile,
                                 HttpServletRequest request) {
        try {
            String IP = IpUtil.getIpAddr(request);
            String token = request.getHeader("token");
            String userid = JwtUtil.verify(token, IP);
            if(userid==null){
                return null;
            }
            // 获取项目路径
            String realPath = request.getSession().getServletContext()
                    .getRealPath("");
            InputStream inputStream = multipartFile.getInputStream();
            String contextPath = request.getServletContext().getContextPath();
            // 服务器根目录的路径
//            String path = realPath.replace(contextPath.substring(1), "");
            // 根目录下新建文件夹upload，存放上传图片
            String uploadPath = "/upload/image/"+userid;
            // 获取文件名称
            String filename = multipartFile.getOriginalFilename();
            UUID uuid=UUID.randomUUID();
            String str[]=filename.split("\\.");
            String randomFileName=uuid.toString()+"."+str[1];
            // 将文件上传的服务器根目录下的upload文件夹
            File file = new File(uploadPath, randomFileName);
            FileUtils.copyInputStreamToFile(inputStream, file);
            inputStream.close();
            // 返回图片访问路径
            String url = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath() + "/image/" +userid+"/"+ randomFileName;

            WangImgResponse res = new WangImgResponse();
            res.setErrno(0);
            List<WangEditImg> list=new ArrayList<WangEditImg>();
            WangEditImg we=new WangEditImg();
            we.setUrl(url);
            list.add(we);
            res.setData(list);
            return res;
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/image/{folder}/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable("folder") String folder,@PathVariable("name") String fileName) {
        try {
            File file = new File("/upload/image/"+folder+"/"+fileName);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes=new byte[inputStream.available()];
            inputStream.read(bytes,0, inputStream.available());
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
