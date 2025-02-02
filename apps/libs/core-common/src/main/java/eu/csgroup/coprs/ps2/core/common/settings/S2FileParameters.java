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

package eu.csgroup.coprs.ps2.core.common.settings;

public final class S2FileParameters {

    public static final String SAD_REGEX = "^.*AUX_SADATA.*";
    public static final String HKTM_REGEX = "^.*PRD_HKTM.*";
    public static final String DT_REGEX = "^DT.*";
    public static final String L0U_DS_REGEX = "^S2[A|B]_OPER_.*_N00\\.00$";
    public static final String AUX_FILE_EXTENSION = ".DBL";
    public static final String L0C_DS_REGEX = "^S2[A|B]_OPER_MSI_L0__DS.*";
    public static final String L0C_GR_REGEX = "^S2[A|B]_OPER_MSI_L0__GR.*";
    public static final String L0U_GR_REGEX = "^S2[A|B]_OPER_MSI_L0U__GR.*";
    public static final String L0C_GR_REGEX_TEMPLATE = "^S2[A|B]_OPER_MSI_L0__GR_.*_DXX.*";
    public static final String L1_DS_REGEX = "^S2[A|B]_OPER_MSI_L1[A|B|C]_DS_" + JobParameters.PROC_STATION + ".*";
    public static final String L1_GR_REGEX = "^S2[A|B]_OPER_MSI_L1[A|B|C]_GR_" + JobParameters.PROC_STATION + ".*";
    public static final String L1A_DS_REGEX = "^S2[A|B]_OPER_MSI_L1A_DS_" + JobParameters.PROC_STATION + ".*";
    public static final String L1A_GR_REGEX = "^S2[A|B]_OPER_MSI_L1A_GR_" + JobParameters.PROC_STATION + ".*";
    public static final String L1B_DS_REGEX = "^S2[A|B]_OPER_MSI_L1B_DS_" + JobParameters.PROC_STATION + ".*";
    public static final String L1B_GR_REGEX = "^S2[A|B]_OPER_MSI_L1B_GR_" + JobParameters.PROC_STATION + ".*";
    public static final String L1C_DS_REGEX = "^S2[A|B]_OPER_MSI_L1C_DS_" + JobParameters.PROC_STATION + ".*";
    public static final String L1C_TL_REGEX = "^S2[A|B]_OPER_MSI_L1C_TL_" + JobParameters.PROC_STATION + ".*";
    public static final String L1C_TC_REGEX = "^S2[A|B]_OPER_MSI_L1C_TC_" + JobParameters.PROC_STATION + ".*\\.jp2$";
    public static final String L2A_DS_REGEX = "^S2[A|B]_OPER_MSI_L2A_DS_" + JobParameters.PROC_STATION + ".*\\.00$";
    public static final String L2A_DS_TAR_REGEX = "^S2[A|B]_OPER_MSI_L2A_DS_" + JobParameters.PROC_STATION + ".*\\.00\\.tar$";
    public static final String L2A_TL_REGEX = "^S2[A|B]_OPER_MSI_L2A_TL_" + JobParameters.PROC_STATION + ".*\\.00$";
    public static final String L2A_TC_REGEX = "^S2[A|B]_OPER_MSI_L2A_TC_" + JobParameters.PROC_STATION + ".*\\.jp2$";
    public static final String L2A_TL_TAR_REGEX = "^S2[A|B]_OPER_MSI_L2A_TL_" + JobParameters.PROC_STATION + ".*\\.00\\.tar$";

    private S2FileParameters() {
    }

}
