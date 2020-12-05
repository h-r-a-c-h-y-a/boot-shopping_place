package am.bdg.boot_shopping_place.service;

import am.bdg.boot_shopping_place.model.Client;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.util.Map;
import java.util.Optional;

public interface AuthService {

    void createUser(Client client) throws FirebaseAuthException;

    Optional<Client> register(Client client);

    Map<String, Object> login(String email, String password) throws FirebaseAuthException;

    UserRecord validateToken(String token) throws FirebaseAuthException;
}
