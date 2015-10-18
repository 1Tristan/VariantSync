package de.ovgu.variantsync.presentationlayer.view.mergeprocess;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.ovgu.variantsync.presentationlayer.controller.ContextController;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;

public class FW1 extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private List list;
	private List list_2;
	private List list_3;
	private String selectedFe;
	private String selectedP;
	private String featureExpressions[];
	ContextController cc = ControllerHandler.getInstance()
			.getContextController();

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public FW1(Shell parent, int style, String featureExpressions[]) {
		super(parent, style);
		setText("SWT Dialog");
		this.featureExpressions = featureExpressions;
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
		shell.setSize(972, 611);
		shell.setText(getText());

		final Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setBounds(45, 50, 91, 23);
		combo.setItems(featureExpressions);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You selected: " + combo.getText());
				selectedFe = combo.getText();
				list_2.setItems(cc.getProjects(combo.getText()).toArray(
						new String[] {}));
			}
		});

		Label lblSelectFeature = new Label(shell, SWT.NONE);
		lblSelectFeature.setBounds(45, 21, 78, 15);
		lblSelectFeature.setText("Select Feature");

		list_2 = new List(shell, SWT.BORDER);
		list_2.setBounds(22, 122, 91, 282);
		list_2.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = list_2.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
				selectedP = list_2.getSelection()[0];
				list_3.setItems(cc.getClasses(selectedFe, selectedP).toArray(
						new String[] {}));
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = list_2.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});

		list_3 = new List(shell, SWT.BORDER);
		list_3.setBounds(119, 122, 98, 282);
		list_3.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = list_3.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);

			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = list_3.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});

		list = new List(shell, SWT.BORDER);
		list.setBounds(223, 122, 152, 282);
		// list.setItems(featureExpressions);
		list.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = list.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = list.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});

		Label lblChanges = new Label(shell, SWT.NONE);
		lblChanges.setBounds(223, 101, 55, 15);
		lblChanges.setText("Changes");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(381, 123, 141, 282);

		Label lblOldCode = new Label(shell, SWT.NONE);
		lblOldCode.setBounds(381, 101, 55, 15);
		lblOldCode.setText("Old Code");

		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(528, 123, 152, 282);

		Label lblNewCode = new Label(shell, SWT.NONE);
		lblNewCode.setBounds(528, 101, 55, 15);
		lblNewCode.setText("New Code");

		List list_1 = new List(shell, SWT.BORDER);
		list_1.setBounds(686, 122, 118, 282);

		Label lblSyncTargets = new Label(shell, SWT.NONE);
		lblSyncTargets.setBounds(686, 101, 67, 15);
		lblSyncTargets.setText("Sync Targets");

		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(815, 123, 141, 282);

		Label lblCodeOfTarget = new Label(shell, SWT.NONE);
		lblCodeOfTarget.setBounds(815, 101, 91, 15);
		lblCodeOfTarget.setText("Code of target");

		Button btnRemoveChangeEntry = new Button(shell, SWT.NONE);
		btnRemoveChangeEntry.setBounds(45, 441, 152, 25);
		btnRemoveChangeEntry.setText("Remove Change Entry");

		Button btnSynchronize = new Button(shell, SWT.NONE);
		btnSynchronize.setBounds(45, 487, 152, 25);
		btnSynchronize.setText("Synchronize");

		Label lblProjects = new Label(shell, SWT.NONE);
		lblProjects.setBounds(22, 101, 55, 15);
		lblProjects.setText("Projects");

		Label lblClasses = new Label(shell, SWT.NONE);
		lblClasses.setBounds(119, 101, 55, 15);
		lblClasses.setText("Classes");

	}
}
