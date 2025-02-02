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

package eu.csgroup.coprs.ps2.core.common.utils;

import org.apache.commons.lang3.StringUtils;

public final class ObsUtils {

    public static final String PREFIX = "s3://";
    public static final String SEPARATOR = "/";

    public static String toUrl(String bucket, String parentKey, String name) {
        return PREFIX + bucket + SEPARATOR + toKey(parentKey, name);
    }

    public static String toKey(String parentKey, String name) {
        return StringUtils.isBlank(parentKey) ? name : parentKey + SEPARATOR + name;
    }

    public static String urlToPath(String url) {
        return StringUtils.substringAfter(url, PREFIX);
    }

    public static String urlToBucket(String url) {
        return StringUtils.substringBefore(urlToPath(url), SEPARATOR);
    }

    public static String urlToKey(String url) {
        return StringUtils.substringAfter(urlToPath(url), SEPARATOR);
    }

    public static String urlToParentKey(String url) {
        final String key = urlToKey(url);
        return key.contains(SEPARATOR) ? StringUtils.substringBeforeLast(key, SEPARATOR) : null;
    }

    public static String urlToName(String url) {
        return StringUtils.substringAfterLast(url, SEPARATOR);
    }

    public static String keyToParentKey(String key) {
        return key.contains(SEPARATOR) ? StringUtils.substringBeforeLast(key, SEPARATOR) : null;
    }

    public static String keyToName(String key) {
        return key.contains(SEPARATOR) ? StringUtils.substringAfterLast(key, SEPARATOR) : key;
    }

    private ObsUtils() {
    }

}
