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

package eu.csgroup.coprs.ps2.ew.l1sb.service.setup;

import eu.csgroup.coprs.ps2.core.common.config.SharedProperties;
import eu.csgroup.coprs.ps2.core.common.model.l1.L1ExecutionInput;
import eu.csgroup.coprs.ps2.core.ew.service.EWSetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class L1sbEWSetupService implements EWSetupService<L1ExecutionInput> {

    private final L1sbEWCleanupService cleanupService;
    private final SharedProperties sharedProperties;

    public L1sbEWSetupService(L1sbEWCleanupService cleanupService, SharedProperties sharedProperties) {
        this.cleanupService = cleanupService;
        this.sharedProperties = sharedProperties;
    }

    @Override
    public void setup(L1ExecutionInput executionInput, UUID parentUid) {

        log.info("Starting setup ...");

        cleanupService.cleanAndPrepare(sharedProperties.getSharedFolderRoot());

        log.info("Finished setup.");
    }

}
