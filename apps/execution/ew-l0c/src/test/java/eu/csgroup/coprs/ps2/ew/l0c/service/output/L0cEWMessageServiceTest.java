package eu.csgroup.coprs.ps2.ew.l0c.service.output;

import eu.csgroup.coprs.ps2.core.common.model.FileInfo;
import eu.csgroup.coprs.ps2.core.common.model.l0.L0cExecutionInput;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProcessingMessage;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProductFamily;
import eu.csgroup.coprs.ps2.core.common.settings.MessageParameters;
import eu.csgroup.coprs.ps2.core.common.test.AbstractTest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class L0cEWMessageServiceTest extends AbstractTest {

    private L0cEWMessageService l0cEWMessageService;

    @Override
    public void setup() throws Exception {
        l0cEWMessageService = new L0cEWMessageService();
    }

    @Override
    public void teardown() throws Exception {

    }

    @Test
    void build() {
        // Given
        final L0cExecutionInput l0uExecutionInput = (L0cExecutionInput) new L0cExecutionInput()
                .setDatastrip("datastrip")
                .setSatellite("A")
                .setStation("foo")
                .setT0PdgsDate(Instant.EPOCH);
        Map<ProductFamily, Set<FileInfo>> fileInfosByFamily = Map.of(
                ProductFamily.S2_L0_GR, Collections.singleton(new FileInfo()),
                ProductFamily.S2_L0_DS, Collections.singleton(new FileInfo())
        );

        // When
        final Set<ProcessingMessage> messages = l0cEWMessageService.build(l0uExecutionInput, fileInfosByFamily, "outputFolder");

        // Then
        assertEquals(2, messages.size());
        assertTrue(messages.stream().allMatch(processingMessage -> processingMessage.getAdditionalFields().containsKey(MessageParameters.T0_PDGS_DATE_FIELD)));
    }

}
