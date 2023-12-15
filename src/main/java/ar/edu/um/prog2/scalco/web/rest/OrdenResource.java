package ar.edu.um.prog2.scalco.web.rest;

import ar.edu.um.prog2.scalco.repository.OrdenRepository;
import ar.edu.um.prog2.scalco.service.OrdenService;
import ar.edu.um.prog2.scalco.service.dto.OrdenDTO;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.prog2.scalco.domain.Orden}.
 */
@RestController
@RequestMapping("/api/ordens")
public class OrdenResource {

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private static final String ENTITY_NAME = "orden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${infoUrl.ordenesUrl}")
    private String urlOrdenes;

    @Value("${infoUrl.token}")
    private String token;

    private final OrdenService ordenService;

    private final OrdenRepository ordenRepository;

    public OrdenResource(OrdenService ordenService, OrdenRepository ordenRepository) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
    }

    /**
     * {@code POST  /ordens} : Create a new orden.
     *
     * @param ordenDTO the ordenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenDTO, or with status {@code 400 (Bad Request)} if the orden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrdenDTO> createOrden(@Valid @RequestBody OrdenDTO ordenDTO) throws URISyntaxException {
        log.debug("REST request to save Orden : {}", ordenDTO);
        if (ordenDTO.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdenDTO result = ordenService.save(ordenDTO);
        return ResponseEntity
            .created(new URI("/api/ordens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordens/:id} : Updates an existing orden.
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrdenDTO> updateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Orden : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdenDTO result = ordenService.update(ordenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordens/:id} : Partial updates given fields of an existing orden, field will ignore if it is null
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ordenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdenDTO> partialUpdateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orden partially : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdenDTO> result = ordenService.partialUpdate(ordenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ordens} : get all the ordens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordens in body.
     */
    @GetMapping("")
    public List<OrdenDTO> getAllOrdens() {
        log.debug("REST request to get all Ordens");
        return ordenService.findAll();
    }

    @GetMapping("/ordenes")
    public List<OrdenDTO> getAllOrdenes() {
        WebClient webClient = WebClient.builder().baseUrl(urlOrdenes).defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();

        // Make a GET request
        String response = webClient.get().retrieve().bodyToMono(String.class).block();

        // Log the response
        log.info("Response from {}: {}", urlOrdenes, response);
        log.debug("REST request to get all Ordens");
        return ordenService.findAll();
    }

    /**
     * {@code GET  /ordens/:id} : get the "id" orden.
     *
     * @param id the id of the ordenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> getOrden(@PathVariable("id") Long id) {
        log.debug("REST request to get Orden : {}", id);
        Optional<OrdenDTO> ordenDTO = ordenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordenDTO);
    }

    /**
     * {@code DELETE  /ordens/:id} : delete the "id" orden.
     *
     * @param id the id of the ordenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable("id") Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
