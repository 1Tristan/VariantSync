package de.ovgu.variantsync.applicationlayer.datamodel.context;

import org.eclipse.swt.graphics.RGB;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.09.2015
 */
public enum CodeHighlighting {

	YELLOW(new RGB(255, 255, 0)), GREEN(new RGB(152, 251, 0)), BLUE(new RGB(
			135, 206, 235)), GREY(new RGB(179, 179, 179)), SALMON(new RGB(255,
			140, 105)), ANTIQUEWHITE(new RGB(250, 235, 215)), SPRINGGREEN(
			new RGB(0, 255, 127)), DARKSLATEGRAY(new RGB(151, 255, 255)), DEFAULTCONTEXT(
			new RGB(255, 255, 255));

	private RGB color;

	CodeHighlighting(RGB color) {
		this.color = color;
	}

	public RGB getColor() {
		return this.color;
	}

	public String getColorName() {
		return this.name().toLowerCase();
	}
}
