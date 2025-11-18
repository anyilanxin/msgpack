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
package com.anyilanxin.msgpack.property;

import com.anyilanxin.msgpack.value.BaseValue;
import com.anyilanxin.msgpack.value.MapValue;
import com.anyilanxin.msgpack.value.StringValue;
import com.anyilanxin.msgpack.value.ValueMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author zxuanhong
 */
public final class MapProperty<K extends BaseValue, V extends BaseValue>
    extends BaseProperty<MapValue<K, V>> implements ValueMap<K, V> {
  public MapProperty(
      final String keyString, final Supplier<K> keyFactory, final Supplier<V> valueFactory) {
    super(keyString, new MapValue<>(keyFactory, valueFactory));
    isSet = true;
  }

  public MapProperty(
      final StringValue key, final Supplier<K> keyFactory, final Supplier<V> valueFactory) {
    super(key, new MapValue<>(keyFactory, valueFactory));
    isSet = true;
  }

  public MapProperty(
      final int initialCapacity,
      final String keyString,
      final Supplier<K> keyFactory,
      final Supplier<V> valueFactory) {
    super(keyString, new MapValue<>(initialCapacity, keyFactory, valueFactory));
    isSet = true;
  }

  public MapProperty(
      final int initialCapacity,
      final StringValue key,
      final Supplier<K> keyFactory,
      final Supplier<V> valueFactory) {
    super(key, new MapValue<>(initialCapacity, keyFactory, valueFactory));
    isSet = true;
  }

  @Override
  public void reset() {
    super.reset();
    isSet = true;
  }

  @Override
  public void put(final Consumer<K> key, final Consumer<V> value) {
    final K keyInfo = this.value.key();
    final V valueInfo = this.value.value();
    key.accept(keyInfo);
    value.accept(valueInfo);
    this.value.put(keyInfo, valueInfo);
  }

  @Override
  public void forEach(final BiConsumer<? super K, ? super V> action) {
    value.forEach(action);
  }

  public V get(final K key) {
    return value.get(key);
  }

  public void remove(final K key) {
    value.remove(key);
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }

  public int size() {
    return value.size();
  }
}
