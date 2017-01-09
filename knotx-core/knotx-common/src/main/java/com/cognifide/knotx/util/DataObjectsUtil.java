/*
 * Knot.x - Reactive microservice assembler - API
 * Copyright (C) 2016 Cognifide Limited
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
package com.cognifide.knotx.util;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import io.vertx.rxjava.core.MultiMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataObjectsUtil {

  /**
   * Equality operator for Multimap objects
   *
   * @param self - compared Multimap object self
   * @param that - compared Multimap object that
   * @return - true if objects are equals, false otherwise
   */
  public static boolean equalsMultimap(MultiMap self, MultiMap that) {
    return Objects.equal(self.names(), that.names()) &&
        self.names().stream().allMatch(name -> that.contains(name) && self.getAll(name).equals(that.getAll(name)));
  }

  public static boolean equalsMap(Map<String, List<String>> self, Map<String, List<String>> that) {
    return Objects.equal(self.keySet(), that.keySet()) &&
        self.values().stream().allMatch(name -> that.containsKey(name) && self.get(name).equals(that.get(name)));
  }

  /**
   * Equality operator for Buffer objects. It assues the buffer might contain binary data
   *
   * @param self - compared Buffer object self
   * @param that - compared Buffer object that
   * @return - true if objects are equals, false otherwise
   */
  public static boolean equalsBody(io.vertx.rxjava.core.buffer.Buffer self, io.vertx.rxjava.core.buffer.Buffer that) {
    if (self == that) {
      return true;
    }
    if (self != null) {
      if (that != null) {
        return Arrays
            .equals(((io.vertx.core.buffer.Buffer) self.getDelegate()).getBytes(), ((io.vertx.core.buffer.Buffer) that.getDelegate()).getBytes());
      } else {
        return false;
      }
    }
    return false;
  }

  /**
   * Method computing hashCode of the give multimap
   *
   * @param multiMap - object to compute hashcode from
   * @return - hashcode of the given Multimap object
   */
  public static int multiMapHash(MultiMap multiMap) {
    return multiMap.names().stream()
        .mapToInt(name -> Optional.ofNullable(multiMap.get(name))
            .map(String::hashCode)
            .orElse(0))
        .reduce(new Integer(0), (sum, hash) -> 31 * sum + hash);
  }

  /**
   * toString() implementation for Multimap object
   *
   * @return String representing given Multimap
   */
  public static String toString(MultiMap multiMap) {
    StringBuilder result = new StringBuilder();
    multiMap.names().stream().forEach(
        name -> result.append(name).append(":").append(Joiner.on(";").join(multiMap.getAll(name))).append("|")
    );

    return result.toString();
  }
}
