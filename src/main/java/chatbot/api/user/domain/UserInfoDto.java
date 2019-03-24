package chatbot.api.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long userId;

    private String providerId;

    private String name;

    private String email;

    private String profileImage;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
