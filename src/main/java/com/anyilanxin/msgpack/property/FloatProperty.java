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

import com.anyilanxin.msgpack.value.FloatValue;

/**
 * @author zxuanhong
 */
public class FloatProperty extends BaseProperty<FloatValue> {
  public FloatProperty(final String key) {
    super(key, new FloatValue());
  }

  public FloatProperty(final String key, final float defaultValue) {
    super(key, new FloatValue(), new FloatValue(defaultValue));
  }

  public float getValue() {
    return resolveValue().getValue();
  }

  public void setValue(final float value) {
    this.value.setValue(value);
    isSet = true;
  }
}
