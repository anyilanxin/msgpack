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

import static com.anyilanxin.msgpack.spec.MsgPackWriter.getEncodedArrayHeaderLenght;

import com.anyilanxin.msgpack.spec.MsgPackReader;
import com.anyilanxin.msgpack.spec.MsgPackWriter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author zxuanhong
 */
public final class SetValue<T extends BaseValue> extends BaseValue {
  private final Set<T> set;
  private final Supplier<T> valueFactory;

  public SetValue(final Supplier<T> valueFactory) {
    this(16, valueFactory);
  }

  public SetValue(final int initialCapacity, final Supplier<T> valueFactory) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }
    this.valueFactory = valueFactory;
    set = new HashSet<>(initialCapacity);
  }

  @Override
  public void reset() {
    set.clear();
  }

  public boolean isEmpty() {
    return set.isEmpty();
  }

  @Override
  public void writeJSON(final StringBuilder builder) {
    builder.append("[");
    int i = 0;
    for (final T value : set) {
      if (i > 0) {
        builder.append(",");
      }

      value.writeJSON(builder);
      i++;
    }
    builder.append("]");
  }

  @Override
  public void write(final MsgPackWriter writer) {
    writer.writeArrayHeader(set.size());
    for (final T item : set) {
      item.write(writer);
    }
  }

  @Override
  public void read(final MsgPackReader reader) {
    reset();

    final var size = reader.readArrayHeader();
    for (int i = 0; i < size; i++) {
      final var value = valueFactory.get();
      value.read(reader);
      set.add(value);
    }
  }

  @Override
  public int getEncodedLength() {
    int length = getEncodedArrayHeaderLenght(set.size());
    for (final T item : set) {
      length += item.getEncodedLength();
    }
    return length;
  }

  @Override
  public int hashCode() {
    return Objects.hash(set);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof final SetValue<?> that)) {
      return false;
    }

    return set.equals(that.set);
  }

  public SetValue<T> add(final Consumer<T> value) {
    final T v = valueFactory.get();
    value.accept(v);
    set.add(v);
    return this;
  }

  public void remove(final T value) {
    set.remove(value);
  }

  public int size() {
    return set.size();
  }
}
