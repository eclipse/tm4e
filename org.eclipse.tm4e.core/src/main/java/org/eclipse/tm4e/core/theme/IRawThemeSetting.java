/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.theme;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A single theme setting.
 *
 * @see <a href="https://github.com/Microsoft/vscode-textmate/blob/master/src/main.ts">
 *      github.com/Microsoft/vscode-textmate/blob/master/src/main.ts</a>
 */
public interface IRawThemeSetting {

	@Nullable
	String getName();

	@Nullable
	Object getScope();

	@Nullable
	IThemeSetting getSetting();

}
