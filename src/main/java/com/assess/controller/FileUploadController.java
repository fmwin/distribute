package com.assess.controller;

import com.assess.util.CodeUtil;
import com.assess.util.FileUtil;
import com.assess.util.ResultMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileUploadController {

    private static Logger logger = Logger.getLogger(FileUploadController.class);

    @RequestMapping(value="/back/uploadLogo", method = RequestMethod.POST)
    @ResponseBody
    public ResultMap uploadLogo(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        ResultMap resultMap = new ResultMap();

        //设置文件不能大于5M
        if (file.getSize() > 5242880){
            resultMap.setCode(CodeUtil.FILE_TOO_BIG);
            resultMap.setDesc("文件太大");
            return resultMap;
        }
        String logoUrl = "http://chuangya.tianjiurc.com/images/logo/default.png";

        String contentType = file.getContentType();   //图片文件类型
        String fileName = file.getOriginalFilename();  //图片名字，暂时先不做处理

        //文件存放路径
        String filePath = "/User/songning/imges/logo/";

        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            logger.error("interface:/back/uploadFile error: ", e);
            resultMap.setDesc("上传文件失败");
            resultMap.setCode(CodeUtil.INNER_ERROR);
        }

        logoUrl = logoUrl.replace("default.png", fileName);

        resultMap.setCode(CodeUtil.SUCCESS);
        resultMap.setDesc("上传文件成功");
        resultMap.setData(logoUrl);
        return resultMap;
    }

}
