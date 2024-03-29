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
 * Sebastian Thomschke (Vegard IT) - add previousLineText support
 */
package org.eclipse.tm4e.languageconfiguration.internal.model;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.internal.utils.StringUtils;

/**
 * Describes a rule to be evaluated when pressing Enter.
 *
 * @see <a href=
 *      "https://github.com/microsoft/vscode/blob/ba2cf46e20df3edf77bdd905acde3e175d985f70/src/vs/editor/common/languages/languageConfiguration.ts#L157">
 *      github.com/microsoft/vscode/blob/main/src/vs/editor/common/languages/languageConfiguration.ts#L157</a>
 */
public final class OnEnterRule {

	/**
	 * This rule will only execute if the text before the cursor matches this regular expression.
	 */
	public final RegExPattern beforeText;

	/**
	 * This rule will only execute if the text after the cursor matches this regular expression.
	 */
	public final @Nullable RegExPattern afterText;

	/**
	 * This rule will only execute if the text above the current line matches this regular expression.
	 */

	public final @Nullable RegExPattern previousLineText;

	/**
	 * The action to execute.
	 */
	public final EnterAction action;

	public OnEnterRule(final RegExPattern beforeText, final @Nullable RegExPattern afterText, final @Nullable RegExPattern previousLineText,
			final EnterAction action) {
		this.beforeText = beforeText;
		this.afterText = afterText;
		this.previousLineText = previousLineText;
		this.action = action;
	}

	@Override
	public String toString() {
		return StringUtils.toString(this, sb -> sb
				.append("beforeText=").append(beforeText).append(", ")
				.append("afterText=").append(afterText).append(", ")
				.append("previousLineText=").append(previousLineText).append(", ")
				.append("action=").append(action));
	}
}
