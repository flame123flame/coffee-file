package coffee.coffee.file.controller;

import coffee.coffee.file.service.FileUploadService;
import coffee.coffee.file.vo.req.Base64FileReq;
import coffee.coffee.file.vo.res.UploadFileRes;
import framework.constant.ResponseConstant;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/img/")
@Slf4j
public class ImgUploadController {


    @Value("${imagesFolder}")
    private String imagesFolder;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    private MyResourceHttpRequestHandler handler;

    // GET PAGINATE
    @PostMapping("upload")
    public ResponseData<UploadFileRes> upload(@RequestBody Base64FileReq param) {
        ResponseData<UploadFileRes> response = new ResponseData<>();
        try {
            response.setData(fileUploadService.uploadImg(param));
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API => ImgUploadController.upload()");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
            log.error("Error Calling API ImgUploadController.upload() :" + e);
        }
        return response;
    }

    // GET PAGINATE
    @PostMapping("upload-multiple")
    public ResponseData<List<UploadFileRes>> upload(@RequestBody List<Base64FileReq> param) {
        ResponseData<List<UploadFileRes>> response = new ResponseData<>();
        try {
            response.setData(fileUploadService.uploadMultipleImg(param));
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API => ImgUploadController.upload()");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
            log.error("Error Calling API ImgUploadController.upload() :" + e);
        }
        return response;
    }

    @GetMapping("/get-img")
    public void getImg(@RequestParam("path") String path, HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        File FILE = new File(this.imagesFolder + "\\" + path);
        request.setAttribute(MyResourceHttpRequestHandler.ATTR_FILE, FILE);
        handler.handleRequest(request, response);
    }

    // GET PAGINATE
    @DeleteMapping("delete")
    public ResponseData<?> delete(@RequestParam("path") String path) {
        ResponseData<?> response = new ResponseData<>();
        try {
            fileUploadService.deleteImg(path);
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.SUCCESS);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API => ImgUploadController.delete()");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.FAILED);
            response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
            log.error("Error Calling API ImgUploadController.delete() :" + e);
        }
        return response;
    }

}
