package am.bdg.boot_shopping_place.repository;

import am.bdg.boot_shopping_place.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByEmailAndPassword(String email, String password);
}
