package am.bdg.boot_shopping_place.service;

import am.bdg.boot_shopping_place.model.Client;
import am.bdg.boot_shopping_place.repository.ClientRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Override
    public Optional<Client> getByUID(String uid) {
        return clientRepository.findById(uid);
    }

    @Override
    public Optional<Client> update(String uid, Client client) {
        client.setUid(uid);
        return Optional.of(clientRepository.save(client));
    }
}
