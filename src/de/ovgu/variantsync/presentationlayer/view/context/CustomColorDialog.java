package de.ovgu.variantsync.presentationlayer.view.context;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.context.IContextOperations;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.09.2015
 */
public class CustomColorDialog {

	private Color color;
	private IContextOperations contextOp = ModuleFactory.getContextOperations();
	private static final CodeHighlighting[] BUTTONS = {
			CodeHighlighting.YELLOW, CodeHighlighting.BLUE,
			CodeHighlighting.ANTIQUEWHITE, CodeHighlighting.SALMON,
			CodeHighlighting.GREEN, CodeHighlighting.GREY,
			CodeHighlighting.SPRINGGREEN, CodeHighlighting.DARKSLATEGRAY,
			CodeHighlighting.DEFAULTCONTEXT };

	public CustomColorDialog(Collection<String> featureExpressions) {
		Shell shell = new Shell(Display.getCurrent(), SWT.APPLICATION_MODAL
				| SWT.SHEET);
		shell.setText("Color Chooser");
		createContents(shell, featureExpressions);
		shell.pack();
		shell.open();
		if (color != null) {
			color.dispose();
		}
	}

	/**
	 * Creates the window contents
	 * 
	 * @param shell
	 *            the parent shell
	 */
	private void createContents(final Shell shell,
			Collection<String> featureExpressions) {
		shell.setLayout(new GridLayout(2, false));

		for (final String fe : featureExpressions) {

			CodeHighlighting ch = contextOp.findColor(fe);
			if (ch == null) {
				ch = CodeHighlighting.YELLOW;
			}

			// final variable to use it in selection adapter
			final CodeHighlighting highlightColor = ch;
			color = new Color(shell.getDisplay(), highlightColor.getColor());

			// Use a label full of spaces to show the color
			final Label colorLabel = new Label(shell, SWT.NONE);
			colorLabel.setText(fe);
			colorLabel.setBackground(color);

			final Combo buttons = new Combo(shell, SWT.DROP_DOWN
					| SWT.READ_ONLY);
			for (int i = 0, n = BUTTONS.length; i < n; i++) {
				buttons.add(BUTTONS[i].getColorName());
			}
			buttons.select(getComboItemId(BUTTONS, fe));
			buttons.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (buttons.getText().equals(
							CodeHighlighting.YELLOW.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.YELLOW);
					} else if (buttons.getText().equals(
							CodeHighlighting.ANTIQUEWHITE.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe,
								CodeHighlighting.ANTIQUEWHITE);
					} else if (buttons.getText().equals(
							CodeHighlighting.BLUE.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.BLUE);
					} else if (buttons.getText().equals(
							CodeHighlighting.GREEN.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.GREEN);
					} else if (buttons.getText().equals(
							CodeHighlighting.GREY.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.GREY);
					} else if (buttons.getText().equals(
							CodeHighlighting.SALMON.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.SALMON);
					} else if (buttons.getText().equals(
							CodeHighlighting.SPRINGGREEN.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe,
								CodeHighlighting.SPRINGGREEN);
					} else if (buttons.getText().equals(
							CodeHighlighting.DARKSLATEGRAY.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe,
								CodeHighlighting.DARKSLATEGRAY);
					} else if (buttons.getText().equals(
							CodeHighlighting.DEFAULTCONTEXT.getColorName())) {
						colorLabel.setBackground(new Color(shell.getDisplay(),
								getColor(buttons.getText())));
						contextOp.setContextColor(fe, CodeHighlighting.DEFAULTCONTEXT);
					}
				}
			});
		}
		Button closeButton = new Button(shell, SWT.NONE);
		closeButton.setText("Close");
		closeButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(
							org.eclipse.swt.events.SelectionEvent e) {
						MarkerHandler.getInstance().refreshMarker(null);
						shell.dispose();
					}
				});
	}

	private int getComboItemId(CodeHighlighting[] items,
			String featureExpression) {
		CodeHighlighting ch = contextOp.findColor(featureExpression);
		if (ch == null) {
			ch = CodeHighlighting.YELLOW;
		}
		int i = 0;
		for (CodeHighlighting item : items) {
			if (item.getColorName().equals(ch.getColorName())) {
				return i;
			}
			i++;
		}
		return 0;
	}

	private RGB getColor(String color) {
		if (color.equals(CodeHighlighting.YELLOW.getColorName())) {
			return CodeHighlighting.YELLOW.getColor();
		} else if (color.equals(CodeHighlighting.ANTIQUEWHITE.getColorName())) {
			return CodeHighlighting.ANTIQUEWHITE.getColor();
		} else if (color.equals(CodeHighlighting.BLUE.getColorName())) {
			return CodeHighlighting.BLUE.getColor();
		} else if (color.equals(CodeHighlighting.GREEN.getColorName())) {
			return CodeHighlighting.GREEN.getColor();
		} else if (color.equals(CodeHighlighting.GREY.getColorName())) {
			return CodeHighlighting.GREY.getColor();
		} else if (color.equals(CodeHighlighting.SALMON.getColorName())) {
			return CodeHighlighting.SALMON.getColor();
		} else if (color.equals(CodeHighlighting.DARKSLATEGRAY.getColorName())) {
			return CodeHighlighting.DARKSLATEGRAY.getColor();
		} else if (color.equals(CodeHighlighting.SPRINGGREEN.getColorName())) {
			return CodeHighlighting.SPRINGGREEN.getColor();
		} else if (color.equals(CodeHighlighting.DEFAULTCONTEXT.getColorName())) {
			return CodeHighlighting.DEFAULTCONTEXT.getColor();
		} else {
			return new RGB(0, 0, 0);
		}
	}
}
