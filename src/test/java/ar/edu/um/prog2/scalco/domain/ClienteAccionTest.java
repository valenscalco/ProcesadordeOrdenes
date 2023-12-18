package ar.edu.um.prog2.scalco.domain;

import static ar.edu.um.prog2.scalco.domain.ClienteAccionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.scalco.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClienteAccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClienteAccion.class);
        ClienteAccion clienteAccion1 = getClienteAccionSample1();
        ClienteAccion clienteAccion2 = new ClienteAccion();
        assertThat(clienteAccion1).isNotEqualTo(clienteAccion2);

        clienteAccion2.setId(clienteAccion1.getId());
        assertThat(clienteAccion1).isEqualTo(clienteAccion2);

        clienteAccion2 = getClienteAccionSample2();
        assertThat(clienteAccion1).isNotEqualTo(clienteAccion2);
    }
}
