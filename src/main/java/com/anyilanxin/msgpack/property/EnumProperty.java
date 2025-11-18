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

import com.anyilanxin.msgpack.value.EnumValue;

public class EnumProperty<E extends Enum<E>> extends BaseProperty<EnumValue<E>> {
  public EnumProperty(final String key, final Class<E> type) {
    super(key, new EnumValue<>(type));
  }

  public EnumProperty(final String key, final Class<E> type, final E defaultValue) {
    super(key, new EnumValue<>(type), new EnumValue<>(type, defaultValue));
  }

  public E getValue() {
    return resolveValue().getValue();
  }

  public void setValue(final E value) {
    this.value.setValue(value);
    isSet = true;
  }
}
