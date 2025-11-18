/*
 * Copyright © 2025 anyilanxin zxh (anyilanxin@aliyun.com)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.msgpack.value;

import com.anyilanxin.msgpack.spec.MsgPackReader;
import com.anyilanxin.msgpack.spec.MsgPackWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class MapValue<K extends BaseValue, V extends BaseValue> extends BaseValue {
  private final Map<K, V> map;
  private final Supplier<K> keyFactory;
  private final Supplier<V> valueFactory;

  public MapValue(final Supplier<K> keyFactory, final Supplier<V> valueFactory) {
      this(16, keyFactory, valueFactory);
  }

    public MapValue(final int initialCapacity, final Supplier<K> keyFactory, final Supplier<V> valueFactory) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        }
        this.keyFactory = keyFactory;
        this.valueFactory = valueFactory;
        map = new HashMap<>(initialCapacity);
    }

  @Override
  public void reset() {
    map.clear();
  }

  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public void writeJSON(final StringBuilder builder) {
    builder.append("{");
    int i = 0;
    for (final var entry : map.entrySet()) {
      if (i > 0) {
        builder.append(",");
      }
      final K key = entry.getKey();
      key.writeJSON(builder);
      builder.append("=");
      final V value = entry.getValue();
      value.writeJSON(builder);
      i++;
    }
    builder.append("}");
  }

  @Override
  public void write(final MsgPackWriter writer) {
    writer.writeMapHeader(map.size());
    for (final Map.Entry<K, V> entry : map.entrySet()) {
      entry.getKey().write(writer);
      entry.getValue().write(writer);
    }
  }

  @Override
  public void read(final MsgPackReader reader) {
    reset();
    final int size = reader.readMapHeader();
    for (int i = 0; i < size; i++) {
      final var key = keyFactory.get();
      key.read(reader);
      final var value = valueFactory.get();
      value.read(reader);
      map.put(key, value);
    }
  }

  @Override
  public int getEncodedLength() {
    return 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(map);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof final MapValue<?, ?> that)) {
      return false;
    }

    return map.equals(that.map);
  }

  public K key() {
    return keyFactory.get();
  }

  public V value() {
    return valueFactory.get();
  }

  public void put(final K key, final V value) {
    map.put(key, value);
  }

  public void forEach(final BiConsumer<? super K, ? super V> action) {
    map.forEach(action);
  }

  public V get(final K key) {
    return map.get(key);
  }

  public void remove(final K key) {
    map.remove(key);
  }

  public int size() {
    return map.size();
  }
}
