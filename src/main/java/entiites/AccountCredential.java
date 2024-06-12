package entiites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
@AllArgsConstructor
public class AccountCredential {

    private Long id;

    private String email;

    private String accessToken;
    private String expiresIn;
    private String scope;
    private String tokenType;
    public AccountCredential(String accessToken){
        this.accessToken = accessToken;
    }
}
