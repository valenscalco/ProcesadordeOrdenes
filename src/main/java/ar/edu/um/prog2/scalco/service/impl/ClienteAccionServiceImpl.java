package ar.edu.um.prog2.scalco.service.impl;

import ar.edu.um.prog2.scalco.domain.ClienteAccion;
import ar.edu.um.prog2.scalco.repository.ClienteAccionRepository;
import ar.edu.um.prog2.scalco.service.ClienteAccionService;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import ar.edu.um.prog2.scalco.service.mapper.ClienteAccionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.prog2.scalco.domain.ClienteAccion}.
 */
@Service
@Transactional
public class ClienteAccionServiceImpl implements ClienteAccionService {

    private final Logger log = LoggerFactory.getLogger(ClienteAccionServiceImpl.class);

    private final ClienteAccionRepository clienteAccionRepository;

    private final ClienteAccionMapper clienteAccionMapper;

    public ClienteAccionServiceImpl(ClienteAccionRepository clienteAccionRepository, ClienteAccionMapper clienteAccionMapper) {
        this.clienteAccionRepository = clienteAccionRepository;
        this.clienteAccionMapper = clienteAccionMapper;
    }

    @Override
    public ClienteAccionDTO save(ClienteAccionDTO clienteAccionDTO) {
        log.debug("Request to save ClienteAccion : {}", clienteAccionDTO);
        ClienteAccion clienteAccion = clienteAccionMapper.toEntity(clienteAccionDTO);
        clienteAccion = clienteAccionRepository.save(clienteAccion);
        return clienteAccionMapper.toDto(clienteAccion);
    }

    @Override
    public ClienteAccionDTO update(ClienteAccionDTO clienteAccionDTO) {
        log.debug("Request to update ClienteAccion : {}", clienteAccionDTO);
        ClienteAccion clienteAccion = clienteAccionMapper.toEntity(clienteAccionDTO);
        clienteAccion = clienteAccionRepository.save(clienteAccion);
        return clienteAccionMapper.toDto(clienteAccion);
    }

    @Override
    public Optional<ClienteAccionDTO> partialUpdate(ClienteAccionDTO clienteAccionDTO) {
        log.debug("Request to partially update ClienteAccion : {}", clienteAccionDTO);

        return clienteAccionRepository
            .findById(clienteAccionDTO.getId())
            .map(existingClienteAccion -> {
                clienteAccionMapper.partialUpdate(existingClienteAccion, clienteAccionDTO);

                return existingClienteAccion;
            })
            .map(clienteAccionRepository::save)
            .map(clienteAccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteAccionDTO> findAll() {
        log.debug("Request to get all ClienteAccions");
        return clienteAccionRepository.findAll().stream().map(clienteAccionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteAccionDTO> findOne(Long id) {
        log.debug("Request to get ClienteAccion : {}", id);
        return clienteAccionRepository.findById(id).map(clienteAccionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClienteAccion : {}", id);
        clienteAccionRepository.deleteById(id);
    }
}
