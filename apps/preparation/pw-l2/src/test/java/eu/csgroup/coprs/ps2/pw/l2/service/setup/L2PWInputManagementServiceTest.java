/*
 * Copyright 2023 CS Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.csgroup.coprs.ps2.pw.l2.service.setup;

import eu.csgroup.coprs.ps2.core.common.exception.FileOperationException;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProcessingMessage;
import eu.csgroup.coprs.ps2.core.common.model.processing.ProductFamily;
import eu.csgroup.coprs.ps2.core.common.model.trace.TraceLogger;
import eu.csgroup.coprs.ps2.core.common.settings.MessageParameters;
import eu.csgroup.coprs.ps2.core.common.test.AbstractTest;
import eu.csgroup.coprs.ps2.core.common.utils.ProcessingMessageUtils;
import eu.csgroup.coprs.ps2.pw.l2.service.prepare.L2DatastripManagementService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class L2PWInputManagementServiceTest extends AbstractTest {

    @Mock
    private L2DatastripManagementService managementService;

    @InjectMocks
    private L2PWInputManagementService inputManagementService;

    private ProcessingMessage processingMessage;

    @Override
    public void setup() throws Exception {
        inputManagementService = new L2PWInputManagementService(managementService);
        processingMessage = ProcessingMessageUtils.create();
    }

    @Override
    public void teardown() throws Exception {
        //
    }

    @Test
    void manageInput_ds() {
        // Given
        processingMessage.setKeyObjectStorage("DS");
        processingMessage.setStoragePath("s3://bucket/path/DS");
        processingMessage.setProductFamily(ProductFamily.S2_L1C_DS);

        // When
        try (LogCaptor logCaptor = LogCaptor.forClass(TraceLogger.class)) {
            final UUID uuid = inputManagementService.manageInput(processingMessage);

            // Then
            verify(managementService).create(any(), any(), any(), any());
            assertEquals(2, logCaptor.getLogs().size());
            assertNotNull(uuid);
        }
    }

    @Test
    void manageInput_tl() {
        // Given
        processingMessage.setKeyObjectStorage("TL");
        processingMessage.setStoragePath("s3://bucket/path/TL");
        processingMessage.setProductFamily(ProductFamily.S2_L1C_TL);
        processingMessage.getMetadata().put(MessageParameters.DATASTRIP_ID_FIELD, "DS");

        // When
        final UUID uuid = inputManagementService.manageInput(processingMessage);

        // Then
        verify(managementService, never()).create(any(), any(), any(), any());
        verify(managementService).updateTLComplete(any());
        assertNotNull(uuid);
    }

    @Test
    void manageInput_aux() {
        // Given
        processingMessage.setKeyObjectStorage("AUX");
        processingMessage.setStoragePath("s3://bucket/path/AUX");
        processingMessage.setProductFamily(ProductFamily.S2_AUX);

        // When
        final UUID uuid = inputManagementService.manageInput(processingMessage);

        // Then
        verify(managementService, never()).create(any(), any(), any(), any());
        verify(managementService, never()).updateTLComplete(any());
        assertNotNull(uuid);
    }

    @Test
    void manageInput_error() {
        // Given
        processingMessage.setKeyObjectStorage("file.xml");
        processingMessage.setStoragePath("s3://bucket/path/file.xml");
        processingMessage.setProductFamily(ProductFamily.S2_L1C_DS);
        doThrow(FileOperationException.class).when(managementService).create(any(), any(), any(), any());

        // When Then
        try (LogCaptor logCaptor = LogCaptor.forClass(TraceLogger.class)) {
            assertThrows(FileOperationException.class, () -> inputManagementService.manageInput(processingMessage));
            final List<String> logs = logCaptor.getLogs();
            assertEquals(2, logs.size());
            assertEquals(1, logs.stream().filter(s -> s.contains("ERROR")).count());
        }
    }

}
