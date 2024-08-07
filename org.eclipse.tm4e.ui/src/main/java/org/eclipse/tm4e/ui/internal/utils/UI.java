/*******************************************************************************
 * Copyright (c) 2021-2024 Vegard IT GmbH and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Sebastian Thomschke (Vegard IT) - initial implementation
 *******************************************************************************/
package org.eclipse.tm4e.ui.internal.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Common UI utilities
 */
public final class UI {

	public static GC createGC() {
		return new GC(getDisplay());
	}

	public static @Nullable IWorkbenchPage getActivePage() {
		final var window = getActiveWindow();
		return window == null ? null : window.getActivePage();
	}

	public static @Nullable IWorkbenchPart getActivePart() {
		final var page = getActivePage();
		return page == null ? null : page.getActivePart();
	}

	public static @Nullable Shell getActiveShell() {
		final var window = getActiveWindow();
		return window == null ? null : window.getShell();
	}

	public static @Nullable ITextEditor getActiveTextEditor() {
		final var activePage = getActivePage();
		if (activePage == null) {
			return null;
		}
		final var editorPart = activePage.getActiveEditor();
		if (editorPart instanceof final ITextEditor textEditor) {
			return textEditor;
		} else if (editorPart instanceof final MultiPageEditorPart multiPageEditorPart) {
			final Object page = multiPageEditorPart.getSelectedPage();
			if (page instanceof final ITextEditor textEditor) {
				return textEditor;
			}
		}
		return null;
	}

	public static @Nullable ITextSelection getActiveTextSelection() {
		final var editor = getActiveTextEditor();
		if (editor == null)
			return null;
		if (editor.getSelectionProvider().getSelection() instanceof final ITextSelection sel)
			return sel;
		return null;
	}

	public static @Nullable ITextViewer getActiveTextViewer() {
		final var editor = getActiveTextEditor();
		if (editor != null) {
			return editor.getAdapter(ITextViewer.class);
		}
		return null;
	}

	public static @Nullable IWorkbenchWindow getActiveWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * @return the current display
	 */
	public static Display getDisplay() {
		if (PlatformUI.isWorkbenchRunning())
			return PlatformUI.getWorkbench().getDisplay();

		final var display = Display.getCurrent();
		if (display != null)
			return display;

		return Display.getDefault();
	}

	public static boolean selectFirstElement(final TableViewer viewer) {
		final var firstElement = viewer.getElementAt(0);
		if (firstElement == null)
			return false;
		viewer.setSelection(new StructuredSelection(firstElement), true);
		return true;
	}

	public static ModifyListener debounce(final int delayMS, final ModifyListener listener) {
		return new ModifyListener() {
			private Runnable later = () -> {
			};

			@Override
			public void modifyText(final ModifyEvent e) {
				final var display = UI.getDisplay();
				// Cancel previous scheduled call
				display.timerExec(-1, later);

				later = () -> listener.modifyText(e);

				// Schedule a new call to execute after the delay
				display.timerExec(delayMS, later);
			}
		};
	}

	public static int getTextWidth(final String string) {
		final GC gc = createGC();
		try {
			gc.setFont(JFaceResources.getDialogFont());
			return gc.stringExtent(string).x;
		} finally {
			gc.dispose();
		}
	}

	/**
	 * @return 0-255
	 */
	private static int getBrightness(final int red, final int green, final int blue) {
		// https://www.w3.org/TR/AERT/#color-contrast
		return (int) (0.299 * red + 0.587 * green + 0.114 * blue);
	}

	public static boolean isDarkColor(final RGB color) {
		return getBrightness(color.red, color.green, color.blue) < 128;
	}

	public static boolean isDarkColor(final Color color) {
		return getBrightness(color.getRed(), color.getGreen(), color.getBlue()) < 128;
	}

	public static boolean isDarkEclipseTheme() {
		final var shell = getActiveShell();
		if (shell == null)
			throw new IllegalStateException("No active shell found!");
		return isDarkColor(shell.getBackground());
	}

	public static boolean isUIThread() {
		return Display.getCurrent() != null;
	}

	/**
	 * Runs the given runnable synchronously on the UI thread
	 *
	 * @throws SWTException if the {@link Display} has been disposed
	 */
	public static <T> T runSync(final Supplier<T> runnable) {
		if (isUIThread())
			return runnable.get();

		final var resultRef = new AtomicReference<T>();
		final var exRef = new AtomicReference<@Nullable RuntimeException>();
		getDisplay().syncExec(() -> {
			try {
				resultRef.set(runnable.get());
			} catch (final RuntimeException ex) {
				exRef.set(ex);
			}
		});

		final RuntimeException ex = exRef.get();
		if (ex != null) {
			throw ex;
		}
		return resultRef.get();
	}

	private UI() {
	}
}
