package saigonuni.dev.resumeBuilder.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saigonuni.dev.resumeBuilder.aop.logexecutiontime.LogExecutionTime;
import saigonuni.dev.resumeBuilder.common.Decorations.Decode;
import saigonuni.dev.resumeBuilder.common.Decorations.UserDC;
import saigonuni.dev.resumeBuilder.controller.base.BaseController;
import saigonuni.dev.resumeBuilder.domain.Resume;
import saigonuni.dev.resumeBuilder.domain.User;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.CreateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.DeleteResumeResponse;
import saigonuni.dev.resumeBuilder.dto.resume.GetResumeAdminResponse;
import saigonuni.dev.resumeBuilder.dto.resume.ListResumeResponse;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminRequest;
import saigonuni.dev.resumeBuilder.dto.resume.UpdateResumeAdminResponse;
import saigonuni.dev.resumeBuilder.service.JwtService;
import saigonuni.dev.resumeBuilder.service.ResumeService;

@Tag(
  name = "Resume Admin Controller",
  description = "Operations pertaining to admin management of resumes"
)
@RestController
@RequestMapping("admin")
public class ResumeAdminController extends BaseController {

  private JwtService jwtService;
  private final ResumeService resumeService;
  private final UserDC userDC;
  private final Decode decode;

  @Autowired
  public ResumeAdminController(
    ResumeService resumeService,
    JwtService jwtService,
    Decode decode,
    UserDC userDC
  ) {
    this.resumeService = resumeService;
    this.jwtService = jwtService;
    this.decode = decode;
    this.userDC = userDC;
  }

  @PostMapping("resumes")
  @Operation(
    summary = "API Thêm Resume mới",
    description = "Returns a list of all resumes"
  )
  @SecurityRequirement(name = "bearerAuth")
  public ResponseEntity<CreateResumeAdminResponse> addResume(
    @Valid @RequestBody CreateResumeAdminRequest request,
    @RequestHeader("Authorization") String authorizationHeader
  ) {
    try {
      User user = userDC.findUserNameByToken(
        decode.AuthenticationDecode(authorizationHeader)
      );
      Resume resume = resumeService.addResume(request, user);
      return ResponseEntity.ok(
        CreateResumeAdminResponse.builder().resume(resume).build()
      );
    } catch (Exception e) {
      throw new RuntimeException("Error processing request: " + e.getMessage());
    }
  }

  @GetMapping("resumes/{id}")
  @LogExecutionTime
  public ResponseEntity<GetResumeAdminResponse> getResumeById(
    @PathVariable String id
  ) {
    Resume resume = resumeService.getResumeById(id);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(GetResumeAdminResponse.builder().resume(resume).build());
  }

  @GetMapping("resumes")
  @LogExecutionTime
  public ResponseEntity<ListResumeResponse> getResume() {
    List<Resume> resumes = resumeService.listResumes();
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(ListResumeResponse.builder().resume(resumes).build());
  }

  @PostMapping("resumes/{id}")
  @Operation(summary = "API Update Resume ", description = "Update API Resume")
  @LogExecutionTime
  public ResponseEntity<UpdateResumeAdminResponse> updateResume(
    @PathVariable String id,
    @RequestBody UpdateResumeAdminRequest request
  ) {
    Resume resume = resumeService.updateResume(id, request);
    return ResponseEntity
      .status(HttpStatus.OK)
      .body(UpdateResumeAdminResponse.builder().resume(resume).build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteResumeResponse> deleteResume(
    @PathVariable String id
  ) {
    resumeService.deleteResume(id);
    return ResponseEntity.ok().build();
  }
  // @PostMapping("resumes")
  // @Operation(
  //   summary = "API Thêm Resume mới",
  //   description = "Returns a list of all resumes"
  // )
  // @SecurityRequirement(name = "bearerAuth")
  // public String addResume(
  //   @RequestHeader("Authorization") String authorizationHeader
  // ) {
  // if (
  //   authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")
  // ) {
  //   System.err.println("Authorization header is missing or invalid");
  //   return "Error: Missing or invalid Authorization header"; // Nên trả về lỗi HTTP 401 hoặc 400
  // }
  // String token = authorizationHeader.substring(7); // Dùng substring(7) an toàn hơn replace
  // System.err.println("Token: " + token);

  // String username = null;
  // try {
  //   // *** Dòng quan trọng cần kiểm tra ***
  //   username = jwtService.extractUsername(token);

  //   // Nếu chạy đến đây thành công, tức là extractUsername không lỗi
  //   System.out.println("Successfully extracted username: " + username);
  //   System.out.println("User found email " + username); // Tên biến là username nhưng bạn in là email?
  //   System.out.println("User accessing API: " + username);

  //   // --- Thêm logic xử lý thêm resume của bạn vào đây ---

  //   return "Hello " + username + ", Resume processing started."; // Trả về thông tin hữu ích hơn
  // } catch (io.jsonwebtoken.ExpiredJwtException e) {
  //   System.err.println("JWT Token has expired: " + e.getMessage());
  //   // Trả về lỗi 401 Unauthorized hoặc thông báo lỗi cụ thể
  //   return "Error: Token has expired";
  // } catch (io.jsonwebtoken.JwtException e) {
  //   // Bắt các lỗi JWT khác (sai chữ ký, sai định dạng,...)
  //   System.err.println("JWT Token is invalid: " + e.getMessage());
  //   // Trả về lỗi 401 Unauthorized hoặc 403 Forbidden
  //   return "Error: Invalid Token";
  // } catch (IllegalArgumentException e) {
  //   System.err.println("JWT claims string is empty: " + e.getMessage());
  //   return "Error: Invalid Token data";
  // } catch (Exception e) {
  //   // Bắt các lỗi không mong muốn khác
  //   System.err.println(
  //     "An error occurred during token processing: " + e.getMessage()
  //   );
  //   e.printStackTrace(); // In stack trace để debug
  //   // Trả về lỗi 500 Internal Server Error
  //   return "Error: Internal Server Error";
  // }
  // decode.AuthenticationDecode(authorizationHeader);
  // return decode.AuthenticationDecode(authorizationHeader);

  // try {
  //   // Sử dụng TokenDecoder để giải mã token
  //   String username = decode.AuthenticationDecode(authorizationHeader);

  //   // Nếu giải mã thành công, thực hiện logic thêm resume
  //   System.out.println("User accessing API: " + username);
  //   userDC.findUserNameByToken(
  //     decode.AuthenticationDecode(authorizationHeader)
  //   );
  //   return "Hello " + username + ", Resume processing started.";
  // } catch (IllegalArgumentException e) {
  //   // Xử lý lỗi và trả về phản hồi phù hợp
  //   return "Error: " + e.getMessage();
  // }
  // }
}
