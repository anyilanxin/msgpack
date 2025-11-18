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

public class EnumValue<E extends Enum<E>> extends BaseValue {
    private final IntegerValue decodedValue = new IntegerValue();
    private final IntegerValue[] binaryEnumValues;
    private final E[] enumConstants;
    private final int size;

    private E value;

    public EnumValue(final Class<E> e, final E defaultValue) {
        enumConstants = e.getEnumConstants();
        size = enumConstants.length;
        binaryEnumValues = new IntegerValue[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            final E constant = enumConstants[i];
            binaryEnumValues[i] = new IntegerValue(constant.ordinal());
        }

        value = defaultValue;
    }

    public EnumValue(final Class<E> e) {
        this(e, null);
    }

    public E getValue() {
        return value;
    }

    public void setValue(final E val) {
        value = val;
    }

    @Override
    public void reset() {
        value = null;
    }

    @Override
    public int getEncodedLength() {
        return MsgPackWriter.getEncodedLongValueLength(value.ordinal());
    }

    @Override
    public void writeJSON(final StringBuilder builder) {
        builder.append("\"");
        builder.append(value.name());
        builder.append("\"");
    }

    @Override
    public void write(final MsgPackWriter writer) {
        binaryEnumValues[value.ordinal()].write(writer);
    }

    @Override
    public void read(final MsgPackReader reader) {
        decodedValue.read(reader);
        final int ordinal = decodedValue.value;
        if (ordinal > size) {
            throw new RuntimeException(String.format("Illegal enum value: %s.", decodedValue));
        }
        value = enumConstants[ordinal];
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof final EnumValue<?> enumValue)) {
            return false;
        }
        return Objects.equals(getValue(), enumValue.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
