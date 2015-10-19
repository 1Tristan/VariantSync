package de.ovgu.variantsync.presentationlayer.view.mergeprocess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeChange;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.presentationlayer.controller.ContextController;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;

public class FeatureView extends ViewPart {

	private List projects;
	private List classes;
	private List changes;
	private List syncTargets;
	private String selectedFeatureExpression;
	private String selectedProject;
	private String selectedClass;
	private int selectedChange;
	private Collection<CodeChange> collChanges;
	private String featureExpressions[];
	private ContextController cc = ControllerHandler.getInstance()
			.getContextController();
	private FeatureController fc = ControllerHandler.getInstance()
			.getFeatureController();
	private Text oldCode;
	private Text newCode;
	private Text codeOfTarget;

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
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3,
				1);
		gd_combo.widthHint = 189;
		combo.setLayoutData(gd_combo);
		combo.setItems(featureExpressions);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("You selected: " + combo.getText());
				selectedFeatureExpression = combo.getText();
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
		gd_list.heightHint = 94;
		gd_list.widthHint = 130;
		projects.setLayoutData(gd_list);
		projects.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				int[] selections = projects.getSelectionIndices();
				String outText = "";
				for (int loopIndex = 0; loopIndex < selections.length; loopIndex++)
					outText += selections[loopIndex] + " ";
				System.out.println("You selected: " + outText);
				selectedProject = projects.getSelection()[0];
				classes.setItems(cc.getClasses(selectedFeatureExpression,
						selectedProject).toArray(new String[] {}));
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
		gd_list_1.heightHint = 174;
		gd_list_1.widthHint = 83;
		classes.setLayoutData(gd_list_1);
		classes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				selectedClass = classes.getSelection()[0];
				collChanges = cc.getChanges(selectedFeatureExpression,
						selectedProject, selectedClass);
				java.util.List<String> timestamps = new ArrayList<String>();
				SimpleDateFormat formatter = new SimpleDateFormat(
						"hh:mm:ss 'at' dd.MM.yyyy");
				for (CodeChange ch : collChanges) {
					timestamps.add(formatter.format(new Date(ch.getTimestamp())));
				}
				changes.setItems(timestamps.toArray(new String[] {}));

				syncTargets.setItems(cc.getSyncTargets(
						selectedFeatureExpression, selectedProject,
						selectedClass).toArray(new String[] {}));
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		new Label(arg0, SWT.NONE);

		changes = new List(arg0, SWT.H_SCROLL | SWT.BORDER);
		GridData gd_changes = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_changes.heightHint = 160;
		gd_changes.widthHint = 93;
		changes.setLayoutData(gd_changes);
		changes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				selectedChange = changes.getSelectionIndex();
				Iterator<CodeChange> it = collChanges.iterator();
				int i = 0;
				oldCode.setText("");
				newCode.setText("");
				while (it.hasNext()) {
					CodeChange ch = it.next();
					if (i == selectedChange) {
						java.util.List<CodeLine> code = ch.getBaseVersion();
						oldCode.setText("");
						for (CodeLine cl : code) {
							oldCode.append(cl.getLine() + ": " + cl.getCode());
							oldCode.append(System.getProperty("line.separator"));
						}
						code = ch.getNewVersion();
						newCode.setText("");
						for (CodeLine cl : code) {
							newCode.append(cl.getLine() + ": " + cl.getCode());
							newCode.append(System.getProperty("line.separator"));
						}
						break;
					}
					i++;
				}
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

		oldCode = new Text(arg0, SWT.BORDER | SWT.V_SCROLL);
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 171;
		oldCode.setLayoutData(gd_text);
		new Label(arg0, SWT.NONE);

		newCode = new Text(arg0, SWT.BORDER | SWT.V_SCROLL);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1);
		gd_text_1.heightHint = 167;
		newCode.setLayoutData(gd_text_1);
		new Label(arg0, SWT.NONE);

		syncTargets = new List(arg0, SWT.BORDER | SWT.H_SCROLL);
		GridData gd_syncTargets = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_syncTargets.heightHint = 174;
		gd_syncTargets.widthHint = 100;
		syncTargets.setLayoutData(gd_syncTargets);
		syncTargets.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				String selection = syncTargets.getSelection()[0];
				String[] tmp = selection.split(":");
				String projectName = tmp[0].trim();
				String className = tmp[1].trim();
				java.util.List<CodeLine> code = cc.getTargetCode(
						selectedFeatureExpression, projectName, className);
				codeOfTarget.setText("");
				for (CodeLine cl : code) {
					codeOfTarget.append(cl.getLine() + ": " + cl.getCode());
					codeOfTarget.append(System.getProperty("line.separator"));
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		new Label(arg0, SWT.NONE);

		codeOfTarget = new Text(arg0, SWT.BORDER | SWT.V_SCROLL);
		gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text.heightHint = 173;
		codeOfTarget.setLayoutData(gd_text);
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
	}
}
