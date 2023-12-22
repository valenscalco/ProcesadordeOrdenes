package ar.edu.um.prog2.scalco.service.impl;

import ar.edu.um.prog2.scalco.domain.Orden;
import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.OrdenService;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
import ar.edu.um.prog2.scalco.service.mapper.OrdenMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ar.edu.um.prog2.scalco.domain.Orden}.
 */
@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;

    public OrdenServiceImpl(OrdenRepository ordenRepository, OrdenMapper ordenMapper) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
    }

    @Override
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.info("Request to save Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    @Override
    public OrdenDTO update(OrdenDTO ordenDTO) {
        log.info("Request to update Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    @Override
    public Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO) {
        log.info("Request to partially update Orden : {}", ordenDTO);

        return ordenRepository
            .findById(ordenDTO.getId())
            .map(existingOrden -> {
                ordenMapper.partialUpdate(existingOrden, ordenDTO);

                return existingOrden;
            })
            .map(ordenRepository::save)
            .map(ordenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenDTO> findAll() {
        log.info("Request to get all Ordens");
        return ordenRepository.findAll().stream().map(ordenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOne(Long id) {
        log.info("Request to get Orden : {}", id);
        return ordenRepository.findById(id).map(ordenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.info("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }

    @Override
    public OrdenDTO toDTO(Orden orden) {
        log.info("Request to convert to DTO : {}", orden);
        OrdenDTO ordenDTO = ordenMapper.toDto(orden);
        return ordenDTO;
    }
}
