package org.eclipse.tm4e.core.internal.utils;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

public final class CompareUtils {

	public static int strcmp(@Nullable final String a, @Nullable final String b) {
		if (a == null && b == null) {
			return 0;
		}
		if (a == null) {
			return -1;
		}
		if (b == null) {
			return 1;
		}
		final int result = a.compareTo(b);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}

	public static int strArrCmp(@Nullable final List<String> a, @Nullable final List<String> b) {
		if (a == null && b == null) {
			return 0;
		}
		if (a == null) {
			return -1;
		}
		if (b == null) {
			return 1;
		}
		final int len1 = a.size();
		final int len2 = b.size();
		if (len1 == len2) {
			for (int i = 0; i < len1; i++) {
				final int res = strcmp(a.get(i), b.get(i));
				if (res != 0) {
					return res;
				}
			}
			return 0;
		}
		return len1 - len2;
	}
}
