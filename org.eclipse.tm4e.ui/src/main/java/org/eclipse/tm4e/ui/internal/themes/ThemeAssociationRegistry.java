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
package org.eclipse.tm4e.ui.internal.themes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tm4e.ui.themes.IThemeAssociation;

/**
 * Theme association registry.
 *
 */
final class ThemeAssociationRegistry {

	private final Map<String, EclipseThemeAssociation> scopes = new HashMap<>();

	private static final class EclipseThemeAssociation {

		private IThemeAssociation light;
		private IThemeAssociation dark;

		IThemeAssociation getLight() {
			return light;
		}

		void setLight(final IThemeAssociation light) {
			this.light = light;
		}

		IThemeAssociation getDark() {
			return dark;
		}

		void setDark(final IThemeAssociation dark) {
			this.dark = dark;
		}

	}


	IThemeAssociation getThemeAssociationFor(final String scopeName, final boolean dark) {
		// From theme assiocations
		IThemeAssociation userAssociation = null;
		final EclipseThemeAssociation registry = scopes.get(scopeName);
		if (registry != null) {
			userAssociation = dark ? registry.getDark() : registry.getLight();
		}
		if (userAssociation != null) {
			return userAssociation;
		}
		return null;
	}

	void register(final IThemeAssociation association) {
		final String scopeName = association.getScopeName();
		EclipseThemeAssociation registry = scopes.get(scopeName);
		if (registry == null) {
			registry = new EclipseThemeAssociation();
			scopes.put(scopeName, registry);
		}
		final boolean dark = association.isWhenDark();
		if (dark) {
			registry.setDark(association);
		} else {
			registry.setLight(association);
		}
	}

	void unregister(final IThemeAssociation association) {
		final String scopeName = association.getScopeName();
		final EclipseThemeAssociation registry = scopes.get(scopeName);
		if (registry != null) {
			final boolean dark = association.isWhenDark();
			if (dark) {
				registry.setDark(null);
			} else {
				registry.setLight(null);
			}
		}
	}

	// IThemeAssociation getThemeAssociationFor(String scopeName, String
	// eclipseThemeId) {
	// IThemeAssociation association = null;
	// BaseThemeAssociationRegistry registry = scopes.get(scopeName);
	// if (registry != null) {
	// association = registry.getThemeAssociationFor(eclipseThemeId);
	// if (association == null) {
	// association = registry.getDefaultAssociation();
	// }
	// }
	// if (association == null) {
	// association = super.getThemeAssociationFor(eclipseThemeId);
	// }
	// return association != null ? association : getDefaultAssociation();
	// }

	// IThemeAssociation[] getThemeAssociationsForScope(String scopeName) {
	// BaseThemeAssociationRegistry registry = scopes.get(scopeName);
	// if (registry != null) {
	// // Get the user associations (from preferences)
	// List<IThemeAssociation> userAssociations = registry.getThemeAssociations();
	// // Get the default associations (from plugin)
	// /*List<IThemeAssociation> defaultAssociations =
	// getThemeAssociations().stream()
	// .filter().collect(Collectors.toList());
	// // Add default association if user associations doesn't define it.
	// for (IThemeAssociation defaultAssociation : defaultAssociations) {
	// if (!(contains(userAssociations, defaultAssociation))) {
	// userAssociations.add(defaultAssociation);
	// }
	// }*/
	// return userAssociations.toArray(IThemeAssociation[]::new);
	// }
	// return getThemeAssociations(true);
	// }
	//
	// private boolean contains(List<IThemeAssociation> userAssociations,
	// IThemeAssociation defaultAssociation) {
	//// for (IThemeAssociation userAssociation : userAssociations) {
	//// if (defaultAssociation.getEclipseThemeId() == null) {
	//// if (userAssociation.getEclipseThemeId() == null) {
	//// return true;
	//// }
	//// } else {
	//// if
	// (defaultAssociation.getEclipseThemeId().equals(userAssociation.getEclipseThemeId()))
	// {
	//// return true;
	//// }
	//// }
	//// }
	// return false;
	// }
	//
	// @Override
	List<IThemeAssociation> getThemeAssociations() {
		final List<IThemeAssociation> associations = new ArrayList<>();
		final Collection<EclipseThemeAssociation> eclipseAssociations = scopes.values();
		for (final EclipseThemeAssociation eclipseAssociation : eclipseAssociations) {
			if (eclipseAssociation.getLight() != null) {
				associations.add(eclipseAssociation.getLight());
			}
			if (eclipseAssociation.getDark() != null) {
				associations.add(eclipseAssociation.getDark());
			}
		}
		return associations;
	}

}
