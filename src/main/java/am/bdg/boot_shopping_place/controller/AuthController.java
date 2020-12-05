package am.bdg.boot_shopping_place.controller;

import am.bdg.boot_shopping_place.model.Client;
import am.bdg.boot_shopping_place.service.AuthService;
import am.bdg.boot_shopping_place.service.ClientService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200/*"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Client client) {
        Optional<Client> optionalClient = authService.register(client);
        if (optionalClient.isPresent()) return ResponseEntity.ok(optionalClient.get());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> model) {
        try {
            Map<String, Object> login = authService.login(model.get("email"), model.get("password"));
            if (login.get("client") == null) {
                return  ResponseEntity.status(HttpStatus.NO_CONTENT).body("Wrong password or login");
            }
            return new ResponseEntity<>(login, HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
