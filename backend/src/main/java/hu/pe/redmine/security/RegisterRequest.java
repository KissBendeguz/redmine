package hu.pe.redmine.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;

    @Override
    public String toString(){
        return String.format("{\"email\":\"%s\",\"password\":\"%s\"}",this.email,this.password);
    }
}
