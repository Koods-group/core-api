package ci.koodysgroup.core.controllers;


import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("sign-in")
    public ResponseEntity<ApiResponse<String>> sigIn() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseUtil.created("Bonjour le monde ...."));
    }

}
