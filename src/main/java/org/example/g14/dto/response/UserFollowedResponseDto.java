package org.example.g14.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserFollowedResponseDto extends UserResponseDto {
    private List<UserResponseDto> followed;

    public UserFollowedResponseDto(int user_id, String user_name, List<UserResponseDto> followed) {
        super(user_id, user_name);
        this.followed = followed;
    }
}
