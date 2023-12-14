package ar.edu.um.prog2.scalco.domain;

import static ar.edu.um.prog2.scalco.domain.OrdenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.um.prog2.scalco.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrdenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orden.class);
        Orden orden1 = getOrdenSample1();
        Orden orden2 = new Orden();
        assertThat(orden1).isNotEqualTo(orden2);

        orden2.setId(orden1.getId());
        assertThat(orden1).isEqualTo(orden2);

        orden2 = getOrdenSample2();
        assertThat(orden1).isNotEqualTo(orden2);
    }
}
