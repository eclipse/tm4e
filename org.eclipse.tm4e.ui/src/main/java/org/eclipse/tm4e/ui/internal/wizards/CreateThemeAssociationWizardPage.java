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
package org.eclipse.tm4e.ui.internal.wizards;

import static org.eclipse.tm4e.core.internal.utils.NullSafetyHelper.*;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tm4e.registry.IGrammarDefinition;
import org.eclipse.tm4e.registry.TMEclipseRegistryPlugin;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.internal.TMUIMessages;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionContentProvider;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionLabelProvider;
import org.eclipse.tm4e.ui.internal.widgets.ThemeContentProvider;
import org.eclipse.tm4e.ui.internal.widgets.ThemeLabelProvider;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.ThemeAssociation;

/**
 * Wizard page to create association between grammar and theme.
 */
final class CreateThemeAssociationWizardPage extends AbstractWizardPage {

	private static final String PAGE_NAME = CreateThemeAssociationWizardPage.class.getName();

	@Nullable
	private ComboViewer themeViewer;

	@Nullable
	private ComboViewer grammarViewer;

	@Nullable
	private final IGrammarDefinition initialDefinition;

	@Nullable
	private final IThemeAssociation initialAssociation;

	@Nullable
	private Button whenDarkButton;

	protected CreateThemeAssociationWizardPage(@Nullable final IGrammarDefinition initialDefinition,
			@Nullable final IThemeAssociation initialAssociation) {
		super(PAGE_NAME);
		super.setTitle(TMUIMessages.CreateThemeAssociationWizardPage_title);
		super.setDescription(TMUIMessages.CreateThemeAssociationWizardPage_description);
		this.initialDefinition = initialDefinition;
		this.initialAssociation = initialAssociation;
	}

	@Override
	protected void createBody(final Composite ancestor) {
		final Composite parent = new Composite(ancestor, SWT.NONE);
		parent.setFont(parent.getFont());
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parent.setLayout(new GridLayout(4, false));

		// TextMate theme
		Label label = new Label(parent, SWT.NONE);
		label.setText(TMUIMessages.CreateThemeAssociationWizardPage_theme_text);
		final var themeViewer = this.themeViewer = new ComboViewer(parent);
		themeViewer.setLabelProvider(new ThemeLabelProvider());
		themeViewer.setContentProvider(new ThemeContentProvider());
		themeViewer.setInput(TMUIPlugin.getThemeManager());
		themeViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		themeViewer.getControl().addListener(SWT.Selection, this);

		label = new Label(parent, SWT.NONE);
		label.setText(TMUIMessages.CreateThemeAssociationWizardPage_grammar_text);
		final var grammarViewer = this.grammarViewer = new ComboViewer(parent);
		grammarViewer.setLabelProvider(new GrammarDefinitionLabelProvider());
		grammarViewer.setContentProvider(new GrammarDefinitionContentProvider());
		grammarViewer.setInput(TMEclipseRegistryPlugin.getGrammarRegistryManager());
		grammarViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grammarViewer.getControl().addListener(SWT.Selection, this);

		if (initialDefinition != null) {
			grammarViewer.setSelection(new StructuredSelection(initialDefinition));
		}

		final var whenDarkButton = this.whenDarkButton = new Button(parent, SWT.CHECK);
		whenDarkButton.setText(TMUIMessages.CreateThemeAssociationWizardPage_whenDark_text);
		final GridData data = new GridData();
		data.horizontalSpan = 4;
		whenDarkButton.setLayoutData(data);

		final var initialAssociation = this.initialAssociation;
		if (initialAssociation != null) {
			final ITheme selectedTheme = TMUIPlugin.getThemeManager().getThemeById(initialAssociation.getThemeId());
			if (selectedTheme != null) {
				themeViewer.setSelection(new StructuredSelection(selectedTheme));
			}
			whenDarkButton.setSelection(initialAssociation.isWhenDark());
		}
	}

	@Override
	protected void initializeDefaultValues() {
		setPageComplete(false);
	}

	@Nullable
	@Override
	protected IStatus validatePage(final Event event) {
		final var themeViewer = this.themeViewer;
		if (themeViewer == null || themeViewer.getSelection().isEmpty()) {
			return new Status(IStatus.ERROR, TMUIPlugin.PLUGIN_ID,
					TMUIMessages.CreateThemeAssociationWizardPage_theme_error_required);
		}

		final var grammarViewer = this.grammarViewer;
		if (grammarViewer == null || grammarViewer.getSelection().isEmpty()) {
			return new Status(IStatus.ERROR, TMUIPlugin.PLUGIN_ID,
					TMUIMessages.CreateThemeAssociationWizardPage_grammar_error_required);
		}
		return null;
	}

	IThemeAssociation getThemeAssociation() {
		final String themeId = ((ITheme) castNonNull(themeViewer).getStructuredSelection().getFirstElement()).getId();
		final String scopeName = ((IGrammarDefinition) castNonNull(grammarViewer).getStructuredSelection()
				.getFirstElement()).getScopeName();
		final boolean whenDark = castNonNull(whenDarkButton).getSelection();
		return new ThemeAssociation(themeId, scopeName, whenDark);
	}

}
