package saigonuni.dev.resumeBuilder.dto.User;

public class UpdateUserAdminRequest {

  private String email;
  private String password;
  private String fullName;
  private String phoneNumber;
  private String address;
  private String avatarUrl;
  private String role;

  public UpdateUserAdminRequest(
    String email,
    String password,
    String fullName,
    String phoneNumber,
    String address,
    String avatarUrl,
    String role
  ) {
    this.email = email;
    this.password = password;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.avatarUrl = avatarUrl;
    this.role = role;
  }

  public UpdateUserAdminRequest() {}

  // Getters and Setters
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
