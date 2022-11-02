package eu.csgroup.coprs.ps2.core.common.model.trace.missing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MissingOutputProductType {

    L0_GR("MSI_L0__GR"),
    L0_DS("MSI_L0__DS"),
    L1_GR("MSI_L1__GR"),
    L1_DS("MSI_L1__DS"),
    HKTM("PRD_HKTM"),
    SAD("AUX_SADATA");

    private final String type;

}
