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

import com.anyilanxin.msgpack.value.ShortValue;

public class ShortProperty extends BaseProperty<ShortValue> {
  public ShortProperty(final String key) {
    super(key, new ShortValue());
  }

  public ShortProperty(final String key, final short defaultValue) {
    super(key, new ShortValue(), new ShortValue(defaultValue));
  }

  public short getValue() {
    return resolveValue().getValue();
  }

  public void setValue(final short value) {
    this.value.setValue(value);
    isSet = true;
  }
}
