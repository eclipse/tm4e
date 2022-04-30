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

import static org.eclipse.tm4e.core.internal.utils.MoreCollections.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.tm4e.core.internal.utils.CompareUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * TextMate theme.
 *
 */
public class Theme {

	private static final Splitter BY_COMMA_SPLITTER = Splitter.on(',');
	private static final Splitter BY_SPACE_SPLITTER = Splitter.on(' ');

	private static final Pattern rrggbb = Pattern.compile("^#[0-9a-f]{6}", Pattern.CASE_INSENSITIVE);
	private static final Pattern rrggbbaa = Pattern.compile("^#[0-9a-f]{8}", Pattern.CASE_INSENSITIVE);
	private static final Pattern rgb = Pattern.compile("^#[0-9a-f]{3}", Pattern.CASE_INSENSITIVE);
	private static final Pattern rgba = Pattern.compile("^#[0-9a-f]{4}", Pattern.CASE_INSENSITIVE);

	private final ColorMap colorMap;
	private final ThemeTrieElement root;
	private final ThemeTrieElementRule defaults;
	private final Map<String /* scopeName */, List<ThemeTrieElementRule>> cache = new HashMap<>();

	public static Theme createFromRawTheme(IRawTheme source) {
		return createFromParsedTheme(parseTheme(source));
	}

	public static List<ParsedThemeRule> parseTheme(IRawTheme source) {
		if (source == null || source.getSettings() == null) {
			return Collections.emptyList();
		}
		// if (!source.settings || !Array.isArray(source.settings)) {
		// return [];
		// }
		Collection<IRawThemeSetting> settings = source.getSettings();
		List<ParsedThemeRule> result = new ArrayList<>();
		int i = 0;
		for (IRawThemeSetting entry : settings) {

			if (entry.getSetting() == null) {
				continue;
			}

			Object settingScope = entry.getScope();
			List<String> scopes;
			if (settingScope instanceof String) {
				String scope = (String) settingScope;

				// remove leading commas
				scope = scope.replaceAll("^[,]+", "");

				// remove trailing commans
				scope = scope.replaceAll("[,]+$", "");

				scopes = BY_COMMA_SPLITTER.splitToList(scope);
			} else if (settingScope instanceof List) {
				scopes = (List<String>) settingScope;
			} else {
				scopes = Arrays.asList("");
			}

			int fontStyle = FontStyle.NotSet;
			Object settingsFontStyle = entry.getSetting().getFontStyle();
			if (settingsFontStyle instanceof String) {
				fontStyle = FontStyle.None;

				Iterable<String> segments = BY_SPACE_SPLITTER.split((String) settingsFontStyle);
				for (String segment : segments) {
					switch (segment) {
					case "italic":
						fontStyle = fontStyle | FontStyle.Italic;
						break;
					case "bold":
						fontStyle = fontStyle | FontStyle.Bold;
						break;
					case "underline":
						fontStyle = fontStyle | FontStyle.Underline;
						break;
					case "strikethrough":
						fontStyle = fontStyle | FontStyle.Strikethrough;
						break;
					}
				}
			}

			String foreground = null;
			Object settingsForeground = entry.getSetting().getForeground();
			if (settingsForeground instanceof String && isValidHexColor((String) settingsForeground)) {
				foreground = (String) settingsForeground;
			}

			String background = null;
			Object settingsBackground = entry.getSetting().getBackground();
			if (settingsBackground instanceof String && isValidHexColor((String) settingsBackground)) {
				background = (String) settingsBackground;
			}
			for (int j = 0, lenJ = scopes.size(); j < lenJ; j++) {
				String _scope = scopes.get(j).trim();

				List<String> segments = BY_SPACE_SPLITTER.splitToList(_scope);

				String scope = getLastElement(segments);
				List<String> parentScopes = null;
				if (segments.size() > 1) {
					parentScopes = segments.subList(0, segments.size() - 1);
					parentScopes = Lists.reverse(parentScopes);
				}

				ParsedThemeRule t = new ParsedThemeRule(scope, parentScopes, i, fontStyle, foreground, background);
				result.add(t);
			}
			i++;
		}

		return result;
	}

	private static boolean isValidHexColor(String hex) {
		if (hex == null || hex.isEmpty()) {
			return false;
		}

		if (rrggbb.matcher(hex).matches()) {
			// #rrggbb
			return true;
		}

		if (rrggbbaa.matcher(hex).matches()) {
			// #rrggbbaa
			return true;
		}

		if (rgb.matcher(hex).matches()) {
			// #rgb
			return true;
		}

		if (rgba.matcher(hex).matches()) {
			// #rgba
			return true;
		}

		return false;
	}

	public static Theme createFromParsedTheme(List<ParsedThemeRule> source) {
		return resolveParsedThemeRules(source);
	}

	/**
	 * Resolve rules (i.e. inheritance).
	 */
	public static Theme resolveParsedThemeRules(List<ParsedThemeRule> parsedThemeRules) {
		// Sort rules lexicographically, and then by index if necessary
		parsedThemeRules.sort((a, b) -> {
			int r = CompareUtils.strcmp(a.scope, b.scope);
			if (r != 0) {
				return r;
			}
			r = CompareUtils.strArrCmp(a.parentScopes, b.parentScopes);
			if (r != 0) {
				return r;
			}
			return a.index - b.index;
		});

		// Determine defaults
		int defaultFontStyle = FontStyle.None;
		String defaultForeground = "#000000";
		String defaultBackground = "#ffffff";
		while (!parsedThemeRules.isEmpty() && "".equals(parsedThemeRules.get(0).scope)) {
			ParsedThemeRule incomingDefaults = parsedThemeRules.remove(0); // shift();
			if (incomingDefaults.fontStyle != FontStyle.NotSet) {
				defaultFontStyle = incomingDefaults.fontStyle;
			}
			if (incomingDefaults.foreground != null) {
				defaultForeground = incomingDefaults.foreground;
			}
			if (incomingDefaults.background != null) {
				defaultBackground = incomingDefaults.background;
			}
		}
		ColorMap colorMap = new ColorMap();
		ThemeTrieElementRule defaults = new ThemeTrieElementRule(0, null, defaultFontStyle,
				colorMap.getId(defaultForeground), colorMap.getId(defaultBackground));

		ThemeTrieElement root = new ThemeTrieElement(new ThemeTrieElementRule(0, null, FontStyle.NotSet, 0, 0),
				Collections.emptyList());
		for (ParsedThemeRule rule : parsedThemeRules) {
			root.insert(0, rule.scope, rule.parentScopes, rule.fontStyle, colorMap.getId(rule.foreground),
					colorMap.getId(rule.background));
		}

		return new Theme(colorMap, defaults, root);
	}

	public Theme(ColorMap colorMap, ThemeTrieElementRule defaults, ThemeTrieElement root) {
		this.colorMap = colorMap;
		this.root = root;
		this.defaults = defaults;
	}

	public Set<String> getColorMap() {
		return this.colorMap.getColorMap();
	}

	public String getColor(int id) {
		return this.colorMap.getColor(id);
	}

	public ThemeTrieElementRule getDefaults() {
		return this.defaults;
	}

	public List<ThemeTrieElementRule> match(String scopeName) {
		if (!this.cache.containsKey(scopeName)) {
			this.cache.put(scopeName, this.root.match(scopeName));
		}
		return this.cache.get(scopeName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cache, colorMap, defaults, root);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Theme other = (Theme) obj;
		return Objects.equals(cache, other.cache) && Objects.equals(colorMap, other.colorMap) &&
				Objects.equals(defaults, other.defaults) && Objects.equals(root, other.root);
	}

}
