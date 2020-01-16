/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.ivyservice;

import java.io.Closeable;
import java.util.Optional;
import java.util.function.BiFunction;

public interface ArtifactCachesProvider extends Closeable {
    String READONLY_CACHE_PROPERTY = "org.gradle.readonly.dependency.cache.path";

    ArtifactCacheMetadata getWritableCacheMetadata();
    Optional<ArtifactCacheMetadata> getReadOnlyCacheMetadata();

    ArtifactCacheLockingManager getWritableCacheLockingManager();
    Optional<ArtifactCacheLockingManager> getReadOnlyCacheLockingManager();

    default <T> T withWritableCache(BiFunction<? super ArtifactCacheMetadata, ? super ArtifactCacheLockingManager, T> function) {
        return function.apply(getWritableCacheMetadata(), getWritableCacheLockingManager());
    }

    default <T> Optional<T> withReadOnlyCache(BiFunction<? super ArtifactCacheMetadata, ? super ArtifactCacheLockingManager, T> function) {
        return getReadOnlyCacheMetadata().map(artifactCacheMetadata -> function.apply(artifactCacheMetadata, getReadOnlyCacheLockingManager().get()));
    }
}
