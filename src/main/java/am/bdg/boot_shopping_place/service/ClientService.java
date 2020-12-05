package am.bdg.boot_shopping_place.service;

import am.bdg.boot_shopping_place.model.Client;

import java.util.Optional;

public interface ClientService {



    Optional<Client> getByUID(String uid);

    Optional<Client> update(String uid, Client client);
}
