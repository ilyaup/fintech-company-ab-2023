package com.academy.fintech.origination.core.service.application.db.client;

import com.academy.fintech.origination.public_interface.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for working with {@link ClientRepository}
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Returns {@link Client} by id, if exits. Otherwise, throws {@link RuntimeException}.
     *
     * @param id of a {@link Client}.
     * @return {@link Client}
     */
    public Client getClientDaoById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id = " + id + " does not exist"));
    }

    public ClientDto getClientById(String id) {
        return toDto(getClientDaoById(id));
    }

    /**
     * Returns client id by email, if exits. Otherwise, throws {@link RuntimeException}.
     *
     * @param email of a {@link Client}.
     * @return {@link Client}
     */
    public String getClientIdByEmail(String email) {
        return clientRepository.findClientByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email = " + email + " does not exist")).getId();
    }

    /**
     * If client exists by email, which contains in {@link ClientDto}, then returns client's id. Otherwise,
     * creates {@link Client} and returns its id.
     *
     * @return client's id.
     */
    public String getClientIdOrCreateClient(ClientDto clientDto) {
        return clientRepository.findClientByEmail(clientDto.email())
                .orElseGet(() -> clientRepository.save(toDao(clientDto))).getId();
    }

    private Client toDao(ClientDto clientDto) {
        return Client.builder()
                .firstName(clientDto.firstName())
                .lastName(clientDto.lastName())
                .email(clientDto.email())
                .salary(clientDto.salary())
                .build();
    }

    private ClientDto toDto(Client client) {
        return ClientDto.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .salary(client.getSalary())
                .build();
    }
}
