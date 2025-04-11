package saigonuni.dev.resumeBuilder.common.Decorations;

import org.springframework.stereotype.Component;
import saigonuni.dev.resumeBuilder.service.JwtService;

@Component
public class Decode {

  private final JwtService jwtService;

  public Decode(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  public String AuthenticationDecode(String authorizationHeader) {
    if (
      authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")
    ) {
      System.err.println("Authorization header is missing or invalid");
      return "Error: Missing or invalid Authorization header";
    }
    String token = authorizationHeader.substring(7);
    System.err.println("Token: " + token);

    String username = null;
    try {
      username = jwtService.extractUsername(token);
   
      return username;
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      System.err.println("JWT Token has expired: " + e.getMessage());
      return "Error: Token has expired";
    } catch (io.jsonwebtoken.JwtException e) {
      System.err.println("JWT Token is invalid: " + e.getMessage());
      return "Error: Invalid Token";
    } catch (IllegalArgumentException e) {
      System.err.println("JWT claims string is empty: " + e.getMessage());
      return "Error: Invalid Token data";
    } catch (Exception e) {
      System.err.println(
        "An error occurred during token processing: " + e.getMessage()
      );
      e.printStackTrace();
      return "Error: Internal Server Error";
    }
  }
}
