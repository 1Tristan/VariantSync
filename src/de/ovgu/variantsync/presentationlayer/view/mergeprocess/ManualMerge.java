package de.ovgu.variantsync.presentationlayer.view.mergeprocess;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;

public class ManualMerge extends Dialog {

	protected Object result;
	protected Shell shell;
	private java.util.List<CodeLine> left;
	private String classLeft;
	private java.util.List<CodeLine> right;
	private String classRight;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ManualMerge(Shell parent, java.util.List<CodeLine> left,
			String classLeft, java.util.List<CodeLine> right, String classRight) {
		super(parent, SWT.NONE);
		setText("SWT Dialog");
		this.classLeft = classLeft;
		this.classRight = classRight;
		this.left = left;
		this.right = right;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(664, 380);
		shell.setText(getText());

		List list = new List(shell, SWT.BORDER);
		list.setBounds(27, 68, 282, 220);
		java.util.List<String> items = new ArrayList<String>();
		for (CodeLine cl : left) {
			if (cl.getCode().contains("<<<<<<<")) {
				items.add("  " + cl.getCode());
				continue;
			}
			items.add(cl.getLine() + ": " + cl.getCode());
		}
		list.setItems(items.toArray(new String[] {}));

		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(346, 68, 295, 220);
		items = new ArrayList<String>();
		items.add("  " + right.get(right.size() - 1).getCode());
		for (CodeLine cl : right) {
			if (!cl.getCode().contains(">>>>>>>"))
				items.add(cl.getLine() + ": " + cl.getCode());
		}
		list_1.setItems(items.toArray(new String[] {}));

		Label lblLeft = new Label(shell, SWT.NONE);
		lblLeft.setBounds(27, 47, 282, 15);
		lblLeft.setText("Left: " + classLeft);

		Label lblRight = new Label(shell, SWT.NONE);
		lblRight.setBounds(346, 47, 295, 15);
		lblRight.setText("Right: " + classRight);

		Button btnTakeLeft = new Button(shell, SWT.NONE);
		btnTakeLeft.setBounds(132, 312, 75, 25);
		btnTakeLeft.setText("Take Left");

		Button btnTakeRight = new Button(shell, SWT.NONE);
		btnTakeRight.setBounds(451, 312, 75, 25);
		btnTakeRight.setText("Take Right");

		Button btnConfirm = new Button(shell, SWT.NONE);
		btnConfirm.setBounds(293, 343, 75, 25);
		btnConfirm.setText("Confirm");

	}
}
