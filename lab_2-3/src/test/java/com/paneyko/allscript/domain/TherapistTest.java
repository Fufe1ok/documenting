package com.paneyko.allscript.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.paneyko.allscript.web.rest.TestUtil;

public class TherapistTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Therapist.class);
        Therapist therapist1 = new Therapist();
        therapist1.setId(1L);
        Therapist therapist2 = new Therapist();
        therapist2.setId(therapist1.getId());
        assertThat(therapist1).isEqualTo(therapist2);
        therapist2.setId(2L);
        assertThat(therapist1).isNotEqualTo(therapist2);
        therapist1.setId(null);
        assertThat(therapist1).isNotEqualTo(therapist2);
    }
}
