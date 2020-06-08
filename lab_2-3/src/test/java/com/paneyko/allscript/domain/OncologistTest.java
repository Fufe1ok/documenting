package com.paneyko.allscript.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.paneyko.allscript.web.rest.TestUtil;

public class OncologistTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Oncologist.class);
        Oncologist oncologist1 = new Oncologist();
        oncologist1.setId(1L);
        Oncologist oncologist2 = new Oncologist();
        oncologist2.setId(oncologist1.getId());
        assertThat(oncologist1).isEqualTo(oncologist2);
        oncologist2.setId(2L);
        assertThat(oncologist1).isNotEqualTo(oncologist2);
        oncologist1.setId(null);
        assertThat(oncologist1).isNotEqualTo(oncologist2);
    }
}
