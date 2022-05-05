/**
 * Copyright (c) 2018 Red Hat Inc. and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Lucas Bullen (Red Hat Inc.) - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration.internal.widgets;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationMessages;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterAction;
import org.eclipse.tm4e.languageconfiguration.internal.supports.OnEnterRule;

final class OnEnterRuleTableWidget extends TableViewer {

	OnEnterRuleTableWidget(final Table table) {
		super(table);
		setContentProvider(new OnEnterRuleContentProvider());
		setLabelProvider(new OnEnterRuleLabelProvider());

		final GC gc = new GC(table.getShell());
		gc.setFont(JFaceResources.getDialogFont());
		final TableColumnLayout columnLayout = new TableColumnLayout();

		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(LanguageConfigurationMessages.OnEnterRuleTableWidget_beforeText);
		int minWidth = computeMinimumColumnWidth(gc, LanguageConfigurationMessages.OnEnterRuleTableWidget_beforeText);
		columnLayout.setColumnData(column1, new ColumnWeightData(2, minWidth, true));

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(LanguageConfigurationMessages.OnEnterRuleTableWidget_afterText);
		minWidth = computeMinimumColumnWidth(gc, LanguageConfigurationMessages.OnEnterRuleTableWidget_afterText);
		columnLayout.setColumnData(column2, new ColumnWeightData(2, minWidth, true));

		final TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText(LanguageConfigurationMessages.OnEnterRuleTableWidget_indentAction);
		minWidth = computeMinimumColumnWidth(gc, LanguageConfigurationMessages.OnEnterRuleTableWidget_indentAction);
		columnLayout.setColumnData(column3, new ColumnWeightData(1, minWidth, true));

		final TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText(LanguageConfigurationMessages.OnEnterRuleTableWidget_appendText);
		minWidth = computeMinimumColumnWidth(gc, LanguageConfigurationMessages.OnEnterRuleTableWidget_appendText);
		columnLayout.setColumnData(column4, new ColumnWeightData(1, minWidth, true));

		final TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setText(LanguageConfigurationMessages.OnEnterRuleTableWidget_removeText);
		minWidth = computeMinimumColumnWidth(gc, LanguageConfigurationMessages.OnEnterRuleTableWidget_removeText);
		columnLayout.setColumnData(column5, new ColumnWeightData(1, minWidth, true));

		gc.dispose();
	}

	private int computeMinimumColumnWidth(final GC gc, final String string) {
		return gc.stringExtent(string).x + 10;
	}

	private static final class OnEnterRuleContentProvider implements IStructuredContentProvider {

		private List<OnEnterRule> onEnterRulesList = Collections.emptyList();

		@Override
		public Object[] getElements(@Nullable final Object input) {
			assert onEnterRulesList != null;
			return onEnterRulesList.toArray(OnEnterRule[]::new);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void inputChanged(@Nullable final Viewer viewer, @Nullable final Object oldInput,
				@Nullable final Object newInput) {
			if (newInput == null) {
				onEnterRulesList = Collections.emptyList();
			} else {
				onEnterRulesList = (List<OnEnterRule>) newInput;
			}
		}

		@Override
		public void dispose() {
			onEnterRulesList = Collections.emptyList();
		}
	}

	private static final class OnEnterRuleLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Nullable
		@Override
		public Image getColumnImage(@Nullable final Object element, final int columnIndex) {
			return null;
		}

		@Nullable
		@Override
		public String getText(@Nullable final Object element) {
			return getColumnText(element, 0);
		}

		@Nullable
		@Override
		public String getColumnText(@Nullable final Object element, final int columnIndex) {
			if (element == null)
				return "";

			final OnEnterRule rule = (OnEnterRule) element;
			final EnterAction action = rule.getAction();

			switch (columnIndex) {
			case 0:
				final var beforeText = rule.getBeforeText();
				return beforeText == null ? "" : beforeText.pattern();
			case 1:
				final var afterText = rule.getAfterText();
				return afterText == null ? "" : afterText.pattern(); //$NON-NLS-1$
			case 2:
				return action.getIndentAction().toString();
			case 3:
				final var appendText = action.getAppendText();
				return appendText == null ? "" : appendText; //$NON-NLS-1$
			case 4:
				final var removeText = action.getRemoveText();
				return removeText == null ? "" : removeText.toString(); //$NON-NLS-1$
			default:
				return ""; //$NON-NLS-1$
			}
		}
	}
}
