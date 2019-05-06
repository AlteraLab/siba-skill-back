package chatbot.api.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevOrder implements Serializable {

    private int devSeq;             // 사용자에게 보여줄 데이터

    @JsonProperty("dev_mac")
    private String devMacAddr;      // 장치의 맥주소

    @JsonProperty("dev_type")
    private String devIdentifier;   // 장치 식별자, 하드웨어 개발자가 지정
                                    // ex) "SAMSUNG TV DS5"
                                    // commercial table에서 조회할때 쓰임
}