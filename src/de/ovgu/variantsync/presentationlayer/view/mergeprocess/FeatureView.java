package de.ovgu.variantsync.presentationlayer.view.mergeprocess;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;

import de.ovgu.variantsync.presentationlayer.controller.ContextController;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;

public class FeatureView extends ViewPart {

	private List projects;
	private List classes;
	private List changes;
	private List oldCode;
	private List newCode;
	private List syncTargets;
	private List codeOfTargets;
	private String selectedFe;
	private String selectedP;
	private String featureExpressions[];
	private ContextController cc = ControllerHandler.getInstance()
			.getContextController();
	private FeatureController fc = ControllerHandler.getInstance()
			.getFeatureController();

	public FeatureView() {
	}

	public void dispose() {
	}

	public void setFocus() {
	}

	@Override
	public void createPartControl(Composite arg0) {
		arg0.setLayout(new GridLayout(13, false));

		Label lblSelectFeatureExpression = new Label(arg0, SWT.NONE);
		lblSelectFeatureExpression.setText("Select Feature Expression");
		new Label(arg0, SWT.NONE);

		featureExpressions = fc.getFeatureExpressions().getFeatureExpressions()
				.toArray(new String[] {});
		final Combo combo = new Combo(arg0, SWT.NONE);
		combo.setItems(featureExpressions);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You selected: " + combo.getText());
				selectedFe = combo.getText();
				projects.setItems(cc.getProjects(combo.getText()).toArray(
						new String[] {}));
			}
		});
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);

		Label lblProjects = new Label(arg0, SWT.NONE);
		lblProjects.setText("Projects");
		new Label(arg0, SWT.NONE);

		Label lblClasses = new Label(arg0, SWT.NONE);
		lblClasses.setText("Classes");
		new Label(arg0, SWT.NONE);

		Label lblChanges = new Label(arg0, SWT.NONE);
		lblChanges.setText("Changes");
		new Label(arg0, SWT.NONE);

		Label lblOldCode = new Label(arg0, SWT.NONE);
		lblOldCode.setText("Old Code");
		new Label(arg0, SWT.NONE);

		Label lblNewCode = new Label(arg0, SWT.NONE);
		lblNewCode.setText("New Code");
		new Label(arg0, SWT.NONE);

		Label lblSyncTargets = new Label(arg0, SWT.NONE);
		lblSyncTargets.setText("Sync Targets");
		new Label(arg0, SWT.NONE);

		Label lblCodeOfTarget = new Label(arg0, SWT.NONE);
		lblCodeOfTarget.setText("Code of Target");

		projects = new List(arg0, SWT.BORDER);
		GridData gd_list = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_list.widthHint = 130;
		projects.setLayoutData(gd_list);
		projects.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = projects.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
				selectedP = projects.getSelection()[0];
				classes.setItems(cc.getClasses(selectedFe, selectedP).toArray(
						new String[] {}));
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = projects.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});

		new Label(arg0, SWT.NONE);

		classes = new List(arg0, SWT.BORDER);
		GridData gd_list_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_list_1.widthHint = 88;
		classes.setLayoutData(gd_list_1);
		classes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = classes.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);

			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = classes.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});
		new Label(arg0, SWT.NONE);

		changes = new List(arg0, SWT.BORDER);
		changes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = changes.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				int[] selections = changes.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
			}
		});
		new Label(arg0, SWT.NONE);

		oldCode = new List(arg0, SWT.BORDER);
		new Label(arg0, SWT.NONE);

		newCode = new List(arg0, SWT.BORDER);
		new Label(arg0, SWT.NONE);

		syncTargets = new List(arg0, SWT.BORDER);
		new Label(arg0, SWT.NONE);

		codeOfTargets = new List(arg0, SWT.BORDER);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);

		Button btnRemoveChangeEntry = new Button(arg0, SWT.NONE);
		btnRemoveChangeEntry.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnRemoveChangeEntry.setText("Remove Change Entry");
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);

		Button btnSynchronize = new Button(arg0, SWT.NONE);
		btnSynchronize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		btnSynchronize.setText("Synchronize");
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		// TODO Auto-generated method stub

	}

}
