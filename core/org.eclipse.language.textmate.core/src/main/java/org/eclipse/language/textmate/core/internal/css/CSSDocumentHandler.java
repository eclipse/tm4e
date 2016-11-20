package org.eclipse.language.textmate.core.internal.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.language.textmate.core.theme.IStyle;
import org.eclipse.language.textmate.core.theme.RGB;
import org.eclipse.language.textmate.core.theme.css.CSSStyle;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

public class CSSDocumentHandler implements DocumentHandler {

	private final List<IStyle> list;
	private CSSStyle currentStyle;

	public CSSDocumentHandler() {
		list = new ArrayList<>();
	}

	@Override
	public void comment(String arg0) throws CSSException {

	}

	@Override
	public void endDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endMedia(SACMediaList arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endSelector(SelectorList selector) throws CSSException {
		currentStyle = null;
	}

	@Override
	public void ignorableAtRule(String arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void importStyle(String arg0, SACMediaList arg1, String arg2) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void namespaceDeclaration(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void property(String name, LexicalUnit value, boolean arg2) throws CSSException {
		if (currentStyle != null) {
			if ("color".equals(name)) {
				RGBColor rgbColor = new RGBColorImpl(value);
				int green = ((int) rgbColor.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
				int red = ((int) rgbColor.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
				int blue = ((int) rgbColor.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
				currentStyle.setColor(new RGB(red, green, blue));
			} else if ("font-weight".equals(name)) {
				currentStyle.setBold(value.getStringValue().toUpperCase().contains("BOLD"));
			} else if ("font-style".equals(name)) {
				currentStyle.setItalic(value.getStringValue().toUpperCase().contains("ITALIC"));
			}
		}
	}

	@Override
	public void startDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startMedia(SACMediaList arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startSelector(SelectorList selector) throws CSSException {
		currentStyle = new CSSStyle(selector);
		list.add(currentStyle);
	}

	public List<IStyle> getList() {
		return list;
	}
}
