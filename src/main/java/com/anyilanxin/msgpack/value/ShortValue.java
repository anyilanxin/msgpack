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
import java.util.Objects;

/**
 * @author zxuanhong
 */
public class ShortValue extends BaseValue {
  protected short value;

  public ShortValue() {
    this((short) 0);
  }

  public ShortValue(final short initialValue) {
    value = initialValue;
  }

  public void setValue(final short val) {
    value = val;
  }

  public short getValue() {
    return value;
  }

  @Override
  public void reset() {
    value = 0;
  }

  @Override
  public void writeJSON(final StringBuilder builder) {
    builder.append(value);
  }

  @Override
  public void write(final MsgPackWriter writer) {
    writer.writeInteger(value);
  }

  @Override
  public void read(final MsgPackReader reader) {
    final long longValue = reader.readInteger();
    if (longValue < Short.MIN_VALUE || longValue > Short.MAX_VALUE) {
      throw new RuntimeException(
          String.format("Value doesn't fit into an integer: %s.", longValue));
    }
    value = (short) longValue;
  }

  @Override
  public int getEncodedLength() {
    return MsgPackWriter.getEncodedLongValueLength(value);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ShortValue)) {
      return false;
    }

    final ShortValue that = (ShortValue) o;
    return getValue() == that.getValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
