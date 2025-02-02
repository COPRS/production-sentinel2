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

package eu.csgroup.coprs.ps2.core.common.service.catalog;

import eu.csgroup.coprs.ps2.core.common.config.CatalogProperties;
import eu.csgroup.coprs.ps2.core.common.exception.CatalogQueryException;
import eu.csgroup.coprs.ps2.core.common.model.catalog.AuxCatalogData;
import eu.csgroup.coprs.ps2.core.common.model.catalog.SessionCatalogData;
import eu.csgroup.coprs.ps2.core.common.model.aux.AuxProductType;
import eu.csgroup.coprs.ps2.core.common.utils.DateUtils;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class CatalogService {

    private static final String AUX_ROUTE = "/metadata/{productFamily}/search";
    private static final String SESSION_ROUTE = "/edrsSession/sessionId/{sessionId}";

    private final CatalogProperties catalogProperties;

    private WebClient webClient;

    public CatalogService(CatalogProperties catalogProperties) {
        this.catalogProperties = catalogProperties;
    }

    @PostConstruct
    public void init() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, catalogProperties.getTimeout() * 1000)
                .responseTimeout(Duration.ofSeconds(catalogProperties.getTimeout()))
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(catalogProperties.getTimeout()))
                        .addHandlerLast(new WriteTimeoutHandler(catalogProperties.getTimeout())));

        webClient = WebClient.builder()
                .baseUrl(catalogProperties.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Optional<AuxCatalogData> retrieveLatestAuxData(AuxProductType productType, String satellite, Instant from, Instant to) {
        log.debug("Retrieving latest AUX data for product type {}", productType.name());
        final List<AuxCatalogData> auxCatalogData = retrieveAuxData(productType, satellite, from, to, null);
        return auxCatalogData.stream().findAny();
    }

    public Optional<AuxCatalogData> retrieveLatestAuxData(AuxProductType productType, String satellite, Instant from, Instant to, String bandIndexId) {
        log.debug("Retrieving latest AUX data for product type {}", productType.name());
        final List<AuxCatalogData> auxCatalogData = retrieveAuxData(productType, satellite, from, to, bandIndexId);
        return auxCatalogData.stream().findAny();
    }

    private List<AuxCatalogData> retrieveAuxData(AuxProductType productType, String satellite, Instant from, Instant to, String bandIndexId) {

        log.debug("Retrieving AUX data for product type {}", productType.name());

        List<AuxCatalogData> auxCatalogDataList = Collections.emptyList();

        AuxCatalogData[] data = query(
                uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path(AUX_ROUTE)
                            .queryParam("productType", productType.name())
                            .queryParam("mode", catalogProperties.getMode())
                            .queryParam("satellite", satellite)
                            .queryParam("t0", DateUtils.toLongDate(from))
                            .queryParam("t1", DateUtils.toLongDate(to));
                    if (StringUtils.hasText(bandIndexId)) {
                        builder = builder.queryParam("bandIndexId", bandIndexId);
                    }
                    return builder.build(catalogProperties.getAuxProductFamily());
                },
                AuxCatalogData[].class);

        if (data != null) {
            auxCatalogDataList = Arrays.stream(data).toList();
        }

        log.debug("Found {} AUX data for product type {}", auxCatalogDataList.size(), productType.name());

        return auxCatalogDataList;
    }

    public List<SessionCatalogData> retrieveSessionData(String sessionId) {

        log.debug("Retrieving SESSION data for session {}", sessionId);

        List<SessionCatalogData> sessionCatalogDataList = Collections.emptyList();

        SessionCatalogData[] data = query(
                uriBuilder -> uriBuilder
                        .path(SESSION_ROUTE)
                        .build(sessionId),
                SessionCatalogData[].class
        );

        if (data != null) {
            sessionCatalogDataList = Arrays.stream(data).toList();
        }

        log.debug("Found {} SESSION data for session {}", sessionCatalogDataList.size(), sessionId);

        return sessionCatalogDataList;
    }

    private <T> T query(Function<UriBuilder, URI> uriFunction, Class<T> clazz) {

        return webClient.get()
                .uri(uriFunction)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        response.createException()
                                .flatMap(e -> Mono.error(new CatalogQueryException("Error querying Catalog: Client Error", e)))
                )
                .onStatus(HttpStatus::is5xxServerError, response ->
                        response.createException()
                                .flatMap(e -> Mono.error(new CatalogQueryException("Error querying Catalog: Server Error", e)))
                )
                .bodyToMono(clazz)
                .retryWhen(
                        Retry.backoff(catalogProperties.getMaxRetry(), Duration.ofSeconds(2))
                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new CatalogQueryException("Error querying Catalog", retrySignal.failure())))
                .block();
    }

}
