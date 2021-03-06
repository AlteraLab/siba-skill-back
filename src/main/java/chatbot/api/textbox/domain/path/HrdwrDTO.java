package chatbot.api.textbox.domain.path;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HrdwrDTO implements Serializable {

    private int hrdwrSeq;             // 사용자에게 보여줄 데이터

    private String hrdwrName;

    private String category;

    private String hrdwrType;

    private String userDefinedName;

    private Long hrdwrId;

    @JsonProperty("dev_type")
    private String authKey;           // userDefinedName 로 auth key 조회

    @JsonAlias("dev_mac")
    private String hrdwrMac;          // DB 에는 없는 데이터이다.
}
