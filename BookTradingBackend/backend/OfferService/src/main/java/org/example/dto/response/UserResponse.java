package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String userName;
    private Double trustPoint;//default = 5.0

}