package coffee.coffee.file.service;

import coffee.coffee.file.vo.req.Base64FileReq;
import coffee.coffee.file.vo.res.UploadFileRes;
import framework.utils.GenerateRandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FileUploadService {

    @Value("${imagesFolder}")
    private String imagesFolder;

    public UploadFileRes uploadImg(Base64FileReq param) throws IOException {
        checkDirExists(imagesFolder);
        String savePath;
        if (param.getPath() != null) {
            savePath = imagesFolder + '\\' + param.getPath();
        }else{
            savePath = imagesFolder;
        }
        checkDirExists(savePath);

        byte[] decodedImg = Base64.getDecoder()
                .decode(removeBase64Prefix(param.getData()).getBytes(StandardCharsets.UTF_8));
        String filename = param.getPrefix() + "-" + GenerateRandomString.generate() + ".jpg";
        Path destinationFile = Paths.get(savePath, filename);
        Files.write(destinationFile, decodedImg);
        UploadFileRes uploadFileRes = new UploadFileRes();
        if (param.getPath() != null) {
            filename =  param.getPath()+'/'+filename;
        }
        uploadFileRes.setSavedPath(filename);
        return uploadFileRes;
    }

    public List<UploadFileRes> uploadMultipleImg(List<Base64FileReq> param) throws IOException {
        checkDirExists(imagesFolder);
        List<UploadFileRes> uploadFileRes = new ArrayList<>();
        for (Base64FileReq item : param
        ) {
            String savePath = imagesFolder + '\\' + item.getPath();
            checkDirExists(savePath);
            byte[] decodedImg = Base64.getDecoder()
                    .decode(removeBase64Prefix(item.getData()).getBytes(StandardCharsets.UTF_8));
            String filename = item.getPrefix() + "-" + GenerateRandomString.generate() + ".jpg";
            Path destinationFile = Paths.get(savePath, filename);
            Files.write(destinationFile, decodedImg);
            UploadFileRes uploadFileRes1 = new UploadFileRes();
            if (item.getPath() != null) {
                filename =  item.getPath()+'/'+filename;
            }
            uploadFileRes1.setSavedPath(filename);
            uploadFileRes.add(uploadFileRes1);
        }
        return uploadFileRes;
    }

    public void deleteImg(String filename) throws IOException {
        Files.deleteIfExists(Paths.get(imagesFolder + '\\' + filename));
    }

    private String removeBase64Prefix(String base64) {
        String res;
        if (base64.startsWith("data:")) {
            String[] strings = base64.split(",");
            res = strings[1];
        } else {
            res = base64;
        }
        return res;
    }

    private void checkDirExists(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
    }
}
