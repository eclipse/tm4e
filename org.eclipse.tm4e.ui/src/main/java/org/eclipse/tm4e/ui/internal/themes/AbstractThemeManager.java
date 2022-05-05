/**
 *  Copyright (c) 2015, 2021 Angelo ZERR and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.ui.internal.themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.tm4e.ui.internal.utils.PreferenceUtils;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;
import org.eclipse.tm4e.ui.themes.ITokenProvider;
import org.eclipse.tm4e.ui.themes.ThemeAssociation;

/**
 * TextMate theme manager implementation.
 *
 */
public abstract class AbstractThemeManager implements IThemeManager {

	private final Map<String /* theme id */ , ITheme> themes = new LinkedHashMap<>();
	private final ThemeAssociationRegistry themeAssociationRegistry = new ThemeAssociationRegistry();

	@Override
	public void registerTheme(final ITheme theme) {
		themes.put(theme.getId(), theme);
	}

	@Override
	public void unregisterTheme(final ITheme theme) {
		themes.remove(theme.getId());
	}

	@Override
	public ITheme getThemeById(final String themeId) {
		return themes.get(themeId);
	}

	@Override
	public ITheme[] getThemes() {
		final Collection<ITheme> themes = this.themes.values();
		return themes.toArray(ITheme[]::new);
	}

	@Override
	public ITheme getDefaultTheme() {
		final boolean dark = isDarkEclipseTheme();
		return getDefaultTheme(dark);
	}

	ITheme getDefaultTheme(final boolean dark) {
		for (final ITheme theme : this.themes.values()) {
			if (theme.isDark() == dark && theme.isDefault()) {
				return theme;
			}
		}
		return null;
	}

	@Override
	public ITheme[] getThemes(final boolean dark) {
		return themes.values().stream().filter(theme -> theme.isDark() == dark)
		   .collect(Collectors.toList()).toArray(ITheme[]::new);
	}

	@Override
	public boolean isDarkEclipseTheme() {
		return isDarkEclipseTheme(PreferenceUtils.getE4PreferenceCSSThemeId());
	}

	@Override
	public boolean isDarkEclipseTheme(final String eclipseThemeId) {
		return eclipseThemeId != null && eclipseThemeId.toLowerCase().contains("dark");
	}

	@Override
	public ITheme getThemeForScope(final String scopeName, final boolean dark) {
		final IThemeAssociation association = themeAssociationRegistry.getThemeAssociationFor(scopeName, dark);
		if (association != null) {
			final String themeId = association.getThemeId();
			return getThemeById(themeId);
		}
		return getDefaultTheme(dark);
	}

	@Override
	public ITheme getThemeForScope(final String scopeName) {
		return getThemeForScope(scopeName, isDarkEclipseTheme());
	}

	@Override
	public IThemeAssociation[] getThemeAssociationsForScope(final String scopeName) {
		final List<IThemeAssociation> associations = new ArrayList<>();
		IThemeAssociation light = themeAssociationRegistry.getThemeAssociationFor(scopeName, false);
		if (light == null) {
			light = new ThemeAssociation(getDefaultTheme(false).getId(), scopeName, false);
		}
		associations.add(light);
		IThemeAssociation dark = themeAssociationRegistry.getThemeAssociationFor(scopeName, true);
		if (dark == null) {
			dark = new ThemeAssociation(getDefaultTheme(true).getId(), scopeName, true);
		}
		associations.add(dark);
		return associations.toArray(IThemeAssociation[]::new);
	}

	@Override
	public void registerThemeAssociation(final IThemeAssociation association) {
		themeAssociationRegistry.register(association);
	}

	@Override
	public void unregisterThemeAssociation(final IThemeAssociation association) {
		themeAssociationRegistry.unregister(association);
	}

	@Override
	public IThemeAssociation[] getAllThemeAssociations() {
		final List<IThemeAssociation> associations = themeAssociationRegistry.getThemeAssociations();
		return associations.toArray(IThemeAssociation[]::new);
	}

	@Override
	public ITokenProvider getThemeForScope(final String scopeName, final RGB background) {
		return getThemeForScope(scopeName, 0.299 * background.red + 0.587 * background.green + 0.114 * background.blue < 128);
	}

}
