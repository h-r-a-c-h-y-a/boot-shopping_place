package am.bdg.boot_shopping_place.service;

import am.bdg.boot_shopping_place.model.Client;
import am.bdg.boot_shopping_place.repository.ClientRepository;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;

    public AuthServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createUser(Client client) throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setDisplayName(client.getName())
                .setEmail(client.getEmail())
                .setPassword(DigestUtils.sha256Hex(client.getPassword()))
                .setEmailVerified(false)
                .setPhoneNumber(client.getPhones()[0])
                .setDisabled(false);
        ApiFuture<UserRecord> userRecord = auth.createUserAsync(request);
    }

    @Override
    public Optional<Client> register(Client client) {
        if (client.getPassword() != null) {
            client.setPassword(DigestUtils.sha256Hex(client.getPassword()));
        }
        return Optional.of(clientRepository.save(client));
    }

    @Override
    public Map<String, Object> login(String email, String password) throws FirebaseAuthException {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        UserRecord userRecord = auth.getUserByEmail(email);
        if (!userRecord.isEmailVerified()) {
            throw new FirebaseAuthException(String.valueOf(HttpStatus.UNAUTHORIZED), "Please verify your email and try again");
        }
        Client client = clientRepository.findByEmailAndPassword(email, DigestUtils.sha256Hex(password)).orElse(null);
        Map<String, Object> map = new HashMap<>();
        map.put("token", auth.createCustomToken(userRecord.getUid()));
        map.put("client", client);
        return map;
    }


    public UserRecord validateToken(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
        return FirebaseAuth.getInstance().getUser(firebaseToken.getUid());
    }
}
