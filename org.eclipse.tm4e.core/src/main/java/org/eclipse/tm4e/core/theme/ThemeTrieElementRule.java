/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThemeTrieElementRule {

	// _themeTrieElementRuleBrand: void;

	public int scopeDepth;
	public final List<String> parentScopes;
	public int fontStyle;
	public int foreground;
	public int background;

	public ThemeTrieElementRule(final int scopeDepth, final List<String> parentScopes, final int fontStyle, final int foreground,
			final int background) {
		this.scopeDepth = scopeDepth;
		this.parentScopes = parentScopes;
		this.fontStyle = fontStyle;
		this.foreground = foreground;
		this.background = background;
	}

	@Override
	public ThemeTrieElementRule clone() {
		return new ThemeTrieElementRule(this.scopeDepth, this.parentScopes, this.fontStyle, this.foreground,
				this.background);
	}

	public static List<ThemeTrieElementRule> cloneArr(final List<ThemeTrieElementRule> arr) {
		final List<ThemeTrieElementRule> r = new ArrayList<>();
		for (int i = 0, len = arr.size(); i < len; i++) {
			r.add(arr.get(i).clone());
		}
		return r;
	}

	public void acceptOverwrite(final int scopeDepth, final int fontStyle, final int foreground, final int background) {
		if (this.scopeDepth > scopeDepth) {
			// TODO!!!
			// console.log('how did this happen?');
		} else {
			this.scopeDepth = scopeDepth;
		}
		// console.log('TODO -> my depth: ' + this.scopeDepth + ', overwriting
		// depth: ' + scopeDepth);
		if (fontStyle != FontStyle.NotSet) {
			this.fontStyle = fontStyle;
		}
		if (foreground != 0) {
			this.foreground = foreground;
		}
		if (background != 0) {
			this.background = background;
		}
	}

	@Override
	public int hashCode() {
	  return Objects.hash(background, fontStyle, foreground, parentScopes, scopeDepth);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ThemeTrieElementRule other = (ThemeTrieElementRule) obj;
		return background == other.background && fontStyle == other.fontStyle && foreground == other.foreground &&
		    Objects.equals(parentScopes, other.parentScopes) && scopeDepth == other.scopeDepth;
	}


}
