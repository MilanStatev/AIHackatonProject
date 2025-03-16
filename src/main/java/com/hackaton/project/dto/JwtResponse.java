public class LoginRequest {
    private String username;
    private String password;
    // Getters and setters
}

public class JwtResponse {
    private String token;
    public JwtResponse(String token) {
        this.token = token;
    }
    // Getter
} 