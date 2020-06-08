package com.paneyko.allscript.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.paneyko.allscript.web.rest.TestUtil;

public class ConclusionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conclusion.class);
        Conclusion conclusion1 = new Conclusion();
        conclusion1.setId(1L);
        Conclusion conclusion2 = new Conclusion();
        conclusion2.setId(conclusion1.getId());
        assertThat(conclusion1).isEqualTo(conclusion2);
        conclusion2.setId(2L);
        assertThat(conclusion1).isNotEqualTo(conclusion2);
        conclusion1.setId(null);
        assertThat(conclusion1).isNotEqualTo(conclusion2);
    }
}
