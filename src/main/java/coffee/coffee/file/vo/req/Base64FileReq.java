package coffee.coffee.file.vo.req;

import lombok.Data;

@Data
public class Base64FileReq {
        private String data;
        private String prefix = "img";
        private String path = null;
}
