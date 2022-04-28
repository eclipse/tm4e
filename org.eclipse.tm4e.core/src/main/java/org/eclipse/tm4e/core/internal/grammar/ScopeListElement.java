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
package org.eclipse.tm4e.core.internal.grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.theme.FontStyle;
import org.eclipse.tm4e.core.theme.ThemeTrieElementRule;

import com.google.common.base.Splitter;

public final class ScopeListElement {

	private static final Splitter BY_SPACE_SPLITTER = Splitter.on(' ');

	@Nullable
	private final ScopeListElement parent;
	private final String scope;
	final int metadata;

	public ScopeListElement(@Nullable ScopeListElement parent, String scope, int metadata) {
		this.parent = parent;
		this.scope = scope;
		this.metadata = metadata;
	}

	private static boolean equals(@Nullable ScopeListElement a, @Nullable ScopeListElement b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		return Objects.equals(a.scope, b.scope) && a.metadata == b.metadata && equals(a.parent, b.parent);
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (other == this) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof ScopeListElement)) {
			return false;
		}
		return ScopeListElement.equals(this, (ScopeListElement)other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(scope, metadata, parent);
	}

	private static boolean matchesScope(String scope, String selector, String selectorWithDot) {
		return (selector.equals(scope) || scope.startsWith(selectorWithDot));
	}

	private static boolean matches(@Nullable ScopeListElement target, List<String> parentScopes) {
		if (parentScopes == null) {
			return true;
		}

		int len = parentScopes.size();
		int index = 0;
		String selector = parentScopes.get(index);
		String selectorWithDot = selector + ".";

		while (target != null) {
			if (matchesScope(target.scope, selector, selectorWithDot)) {
				index++;
				if (index == len) {
					return true;
				}
				selector = parentScopes.get(index);
				selectorWithDot = selector + '.';
			}
			target = target.parent;
		}

		return false;
	}

	public static int mergeMetadata(int metadata, @Nullable ScopeListElement scopesList, ScopeMetadata source) {
		if (source == null) {
			return metadata;
		}

		int fontStyle = FontStyle.NotSet;
		int foreground = 0;
		int background = 0;

		if (source.themeData != null) {
			// Find the first themeData that matches
			for (ThemeTrieElementRule themeData : source.themeData) {
				if (matches(scopesList, themeData.parentScopes)) {
					fontStyle = themeData.fontStyle;
					foreground = themeData.foreground;
					background = themeData.background;
					break;
				}
			}
		}

		return StackElementMetadata.set(metadata, source.languageId, source.tokenType, fontStyle, foreground,
				background);
	}

	private static ScopeListElement push(ScopeListElement target, Grammar grammar, Iterable<String> scopes) {
		for (String scope : scopes) {
			ScopeMetadata rawMetadata = grammar.getMetadataForScope(scope);
			int metadata = ScopeListElement.mergeMetadata(target.metadata, target, rawMetadata);
			target = new ScopeListElement(target, scope, metadata);
		}
		return target;
	}

	ScopeListElement push(Grammar grammar, @Nullable String scope) {
		if (scope == null) {
			return this;
		}
		return ScopeListElement.push(this, grammar, BY_SPACE_SPLITTER.split(scope));
	}

	private static List<String> generateScopes(ScopeListElement scopesList) {
		List<String> result = new ArrayList<>();
		while (scopesList != null) {
			result.add(scopesList.scope);
			scopesList = scopesList.parent;
		}
		Collections.reverse(result);
		return result;
	}

	List<String> generateScopes() {
		return ScopeListElement.generateScopes(this);
	}
}
