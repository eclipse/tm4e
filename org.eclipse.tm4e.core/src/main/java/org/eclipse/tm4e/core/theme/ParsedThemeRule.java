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

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;

/**
 * @see <a href=
 *      "https://github.com/microsoft/vscode-textmate/blob/9157c7f869219dbaf9a5a5607f099c00fe694a29/src/theme.ts#L141">
 *      github.com/Microsoft/vscode-textmate/blob/master/src/theme.ts</a>
 */
public class ParsedThemeRule {

	public final String scope;

	@Nullable
	public final List<String> parentScopes;

	public final int index;

	/**
	 * -1 if not set. An or mask of `FontStyle` otherwise.
	 */
	public final int fontStyle;

	@Nullable
	public final String foreground;

	@Nullable
	public final String background;

	public ParsedThemeRule(final String scope, @Nullable final List<String> parentScopes, final int index,
			final int fontStyle, @Nullable final String foreground, @Nullable final String background) {
		this.scope = scope;
		this.parentScopes = parentScopes;
		this.index = index;
		this.fontStyle = fontStyle;
		this.foreground = foreground;
		this.background = background;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fontStyle;
		result = prime * result + index;
		result = prime * result + Objects.hash(background, foreground, parentScopes, scope);
		return result;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParsedThemeRule other = (ParsedThemeRule) obj;
		return Objects.equals(background, other.background) && fontStyle == other.fontStyle
				&& Objects.equals(foreground, other.foreground) && index == other.index
				&& Objects.equals(parentScopes, other.parentScopes) && Objects.equals(scope, other.scope);
	}
}
