package ar.edu.um.prog2.scalco.web.rest;

import ar.edu.um.prog2.scalco.repository.ClienteAccionRepository;
import ar.edu.um.prog2.scalco.service.ClienteAccionService;
import ar.edu.um.prog2.scalco.service.dto.ClienteAccionDTO;
import ar.edu.um.prog2.scalco.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.prog2.scalco.domain.ClienteAccion}.
 */
@RestController
@RequestMapping("/api/cliente-accions")
public class ClienteAccionResource {

    private final Logger log = LoggerFactory.getLogger(ClienteAccionResource.class);

    private static final String ENTITY_NAME = "clienteAccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClienteAccionService clienteAccionService;

    private final ClienteAccionRepository clienteAccionRepository;

    public ClienteAccionResource(ClienteAccionService clienteAccionService, ClienteAccionRepository clienteAccionRepository) {
        this.clienteAccionService = clienteAccionService;
        this.clienteAccionRepository = clienteAccionRepository;
    }

    /**
     * {@code POST  /cliente-accions} : Create a new clienteAccion.
     *
     * @param clienteAccionDTO the clienteAccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clienteAccionDTO, or with status {@code 400 (Bad Request)} if the clienteAccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClienteAccionDTO> createClienteAccion(@Valid @RequestBody ClienteAccionDTO clienteAccionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ClienteAccion : {}", clienteAccionDTO);
        if (clienteAccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new clienteAccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClienteAccionDTO result = clienteAccionService.save(clienteAccionDTO);
        return ResponseEntity
            .created(new URI("/api/cliente-accions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cliente-accions/:id} : Updates an existing clienteAccion.
     *
     * @param id the id of the clienteAccionDTO to save.
     * @param clienteAccionDTO the clienteAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteAccionDTO,
     * or with status {@code 400 (Bad Request)} if the clienteAccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clienteAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteAccionDTO> updateClienteAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClienteAccionDTO clienteAccionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ClienteAccion : {}, {}", id, clienteAccionDTO);
        if (clienteAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClienteAccionDTO result = clienteAccionService.update(clienteAccionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteAccionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cliente-accions/:id} : Partial updates given fields of an existing clienteAccion, field will ignore if it is null
     *
     * @param id the id of the clienteAccionDTO to save.
     * @param clienteAccionDTO the clienteAccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clienteAccionDTO,
     * or with status {@code 400 (Bad Request)} if the clienteAccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clienteAccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clienteAccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClienteAccionDTO> partialUpdateClienteAccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClienteAccionDTO clienteAccionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClienteAccion partially : {}, {}", id, clienteAccionDTO);
        if (clienteAccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clienteAccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clienteAccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClienteAccionDTO> result = clienteAccionService.partialUpdate(clienteAccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clienteAccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cliente-accions} : get all the clienteAccions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clienteAccions in body.
     */
    @GetMapping("")
    public List<ClienteAccionDTO> getAllClienteAccions() {
        log.debug("REST request to get all ClienteAccions");
        return clienteAccionService.findAll();
    }

    /**
     * {@code GET  /cliente-accions/:id} : get the "id" clienteAccion.
     *
     * @param id the id of the clienteAccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clienteAccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteAccionDTO> getClienteAccion(@PathVariable("id") Long id) {
        log.debug("REST request to get ClienteAccion : {}", id);
        Optional<ClienteAccionDTO> clienteAccionDTO = clienteAccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clienteAccionDTO);
    }

    /**
     * {@code DELETE  /cliente-accions/:id} : delete the "id" clienteAccion.
     *
     * @param id the id of the clienteAccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteAccion(@PathVariable("id") Long id) {
        log.debug("REST request to delete ClienteAccion : {}", id);
        clienteAccionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
