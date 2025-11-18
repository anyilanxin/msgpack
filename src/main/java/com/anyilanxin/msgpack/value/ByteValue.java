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

import static com.anyilanxin.msgpack.spec.MsgPackWriter.getEncodedLongValueLength;

import com.anyilanxin.msgpack.spec.MsgPackReader;
import com.anyilanxin.msgpack.spec.MsgPackWriter;
import java.util.Objects;

/**
 * @author zxuanhong
 */
public class ByteValue extends BaseValue {
  protected byte value;

  public ByteValue() {
    this((byte) 0);
  }

  public ByteValue(final byte initialValue) {
    value = initialValue;
  }

  public void setValue(final byte val) {
    value = val;
  }

  public byte getValue() {
    return value;
  }

  @Override
  public void reset() {
    value = (byte) 0;
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
    value = reader.readByte();
  }

  @Override
  public int getEncodedLength() {
    return getEncodedLongValueLength(value);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof final ByteValue that)) {
      return false;
    }
    return getValue() == that.getValue();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
