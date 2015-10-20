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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeChange;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeHighlighting;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.presentationlayer.controller.ContextController;
import de.ovgu.variantsync.presentationlayer.controller.ControllerHandler;
import de.ovgu.variantsync.presentationlayer.controller.FeatureController;
import de.ovgu.variantsync.presentationlayer.controller.SynchronizationController;

/**
 * 
 *
 * @author Tristan Pfofe (tristan.pfofe@st.ovgu.de)
 * @version 1.0
 * @since 20.10.2015
 */
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
	private SynchronizationController sc = ControllerHandler.getInstance()
			.getSynchronizationController();
	private FeatureController fc = ControllerHandler.getInstance()
			.getFeatureController();
	private Table oldCode;
	private Table newCode;
	private Table codeOfTarget;
	private Table syncPreview;

	public FeatureView() {
	}

	public void dispose() {
	}

	public void setFocus() {
	}

	@Override
	public void createPartControl(Composite arg0) {
		arg0.setLayout(new GridLayout(9, false));

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
				if (syncTargets != null)
					syncTargets.setItems(new String[] {});
				if (changes != null)
					changes.setItems(new String[] {});
				if (classes != null)
					classes.setItems(new String[] {});
				if (codeOfTarget != null)
					codeOfTarget.removeAll();
				if (collChanges != null)
					collChanges.clear();
				if (oldCode != null)
					oldCode.removeAll();
				if (newCode != null)
					newCode.removeAll();
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

		projects = new List(arg0, SWT.BORDER | SWT.H_SCROLL);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_list.heightHint = 255;
		gd_list.widthHint = 119;
		projects.setLayoutData(gd_list);
		projects.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				if (syncTargets != null)
					syncTargets.setItems(new String[] {});
				if (changes != null)
					changes.setItems(new String[] {});
				if (classes != null)
					classes.setItems(new String[] {});
				if (codeOfTarget != null)
					codeOfTarget.removeAll();
				if (oldCode != null)
					oldCode.removeAll();
				if (newCode != null)
					newCode.removeAll();
				selectedProject = projects.getSelection()[0];
				classes.setItems(cc.getClasses(selectedFeatureExpression,
						selectedProject).toArray(new String[] {}));
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});

		new Label(arg0, SWT.NONE);

		classes = new List(arg0, SWT.BORDER | SWT.H_SCROLL);
		GridData gd_list_1 = new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				1);
		gd_list_1.heightHint = 259;
		gd_list_1.widthHint = 83;
		classes.setLayoutData(gd_list_1);
		classes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				if (syncTargets != null)
					syncTargets.setItems(new String[] {});
				if (codeOfTarget != null)
					codeOfTarget.removeAll();
				if (oldCode != null)
					oldCode.removeAll();
				if (newCode != null)
					newCode.removeAll();
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
		GridData gd_changes = new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				1);
		gd_changes.heightHint = 256;
		gd_changes.widthHint = 93;
		changes.setLayoutData(gd_changes);
		changes.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				if (oldCode != null)
					oldCode.removeAll();
				if (newCode != null)
					newCode.removeAll();
				selectedChange = changes.getSelectionIndex();
				Iterator<CodeChange> it = collChanges.iterator();
				int i = 0;
				oldCode.removeAll();
				newCode.removeAll();
				CodeHighlighting ccolor = cc
						.getContextColor(selectedFeatureExpression);
				while (it.hasNext()) {
					CodeChange ch = it.next();
					if (i == selectedChange) {
						java.util.List<CodeLine> code = ch.getBaseVersion();
						oldCode.removeAll();
						for (CodeLine cl : code) {
							TableItem item = new TableItem(oldCode, SWT.NONE);
							item.setText(cl.getLine() + ": " + cl.getCode());
							Color color = new Color(getSite().getShell()
									.getDisplay(), ccolor.getRGB());
							item.setBackground(color);
							// setBackground(ccolor, item);
						}
						code = ch.getNewVersion();
						newCode.removeAll();
						for (CodeLine cl : code) {
							TableItem item = new TableItem(newCode, SWT.NONE);
							item.setText(cl.getLine() + ": " + cl.getCode());
							Color color = new Color(getSite().getShell()
									.getDisplay(), ccolor.getRGB());
							item.setBackground(color);
							// setBackground(ccolor, item);
							if (cl.isNew()) {
								item.setForeground(getSite().getShell()
										.getDisplay()
										.getSystemColor(SWT.COLOR_WHITE));
								item.setBackground(getSite().getShell()
										.getDisplay()
										.getSystemColor(SWT.COLOR_BLACK));
							}
						}
						break;
					}
					i++;
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		new Label(arg0, SWT.NONE);

		oldCode = new Table(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_text.heightHint = 241;
		oldCode.setLayoutData(gd_text);
		new Label(arg0, SWT.NONE);

		newCode = new Table(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_text_1.heightHint = 251;
		newCode.setLayoutData(gd_text_1);
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

		Label lblSyncTargets = new Label(arg0, SWT.NONE);
		lblSyncTargets.setText("Sync Targets");
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);
		new Label(arg0, SWT.NONE);

		Label lblCodeOfTarget = new Label(arg0, SWT.NONE);
		lblCodeOfTarget.setText("Code of Target");
		new Label(arg0, SWT.NONE);

		Label lblSyncPreview = new Label(arg0, SWT.NONE);
		lblSyncPreview.setText("Sync Preview");

		syncTargets = new List(arg0, SWT.BORDER | SWT.H_SCROLL);
		GridData gd_syncTargets = new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1);
		gd_syncTargets.heightHint = 79;
		gd_syncTargets.widthHint = 100;
		syncTargets.setLayoutData(gd_syncTargets);
		syncTargets.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent event) {
				if (codeOfTarget != null)
					codeOfTarget.removeAll();
				String selection = syncTargets.getSelection()[0];
				String[] tmp = selection.split(":");
				String projectName = tmp[0].trim();
				String className = tmp[1].trim();
				java.util.List<CodeLine> code = cc.getTargetCode(
						selectedFeatureExpression, projectName, className);
				CodeHighlighting ccolor = cc
						.getContextColor(selectedFeatureExpression);
				codeOfTarget.removeAll();
				for (CodeLine cl : code) {
					TableItem item = new TableItem(codeOfTarget, SWT.NONE);
					item.setText(cl.getLine() + ": " + cl.getCode());
					Color color = new Color(getSite().getShell().getDisplay(),
							ccolor.getRGB());
					item.setBackground(color);
					// setBackground(ccolor, item);
				}
				Iterator<CodeChange> it = collChanges.iterator();
				java.util.List<CodeLine> newCode = null;
				int i = 0;
				while (it.hasNext()) {
					CodeChange cc = it.next();
					if (i == selectedChange) {
						newCode = cc.getNewVersion();
					}
					i++;
				}
				java.util.List<CodeLine> syncCode = sc
						.doAutoSync(newCode, code);
				for (CodeLine cl : syncCode) {
					TableItem item = new TableItem(syncPreview, SWT.NONE);
					item.setText(cl.getLine() + ": " + cl.getCode());
					Color color = new Color(getSite().getShell().getDisplay(),
							ccolor.getRGB());
					item.setBackground(color);
				}
			}

			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		new Label(arg0, SWT.NONE);

		Button btnSynchronize = new Button(arg0, SWT.NONE);
		btnSynchronize.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		btnSynchronize.setText("Auto Sync");
		new Label(arg0, SWT.NONE);

		Button btnManualSync = new Button(arg0, SWT.NONE);
		btnManualSync.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		btnManualSync.setText("Manual Sync");
		new Label(arg0, SWT.NONE);

		codeOfTarget = new Table(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL);
		gd_text = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_text.heightHint = 247;
		codeOfTarget.setLayoutData(gd_text);
		new Label(arg0, SWT.NONE);

		syncPreview = new Table(arg0, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL);
		gd_text = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_text.heightHint = 111;
		syncPreview.setLayoutData(gd_text);
	}

	private void setBackground(CodeHighlighting foreground, TableItem item) {
		Color color = null;
		final RGB BLACK = new RGB(0, 0, 0);
		final RGB WHITE = new RGB(255, 255, 255);
		if (foreground.getColorName().equals(
				CodeHighlighting.YELLOW.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), BLACK);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.GREEN_BRIGHT.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), BLACK);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.ORANGE.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), BLACK);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.GREEN.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), WHITE);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.RED.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), WHITE);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.PINK.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), WHITE);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.BLUE_BRIGHT.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), BLACK);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.BLUE.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), WHITE);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.PURPLE.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), WHITE);
		} else if (foreground.getColorName().equals(
				CodeHighlighting.DEFAULTCONTEXT.getColorName())) {
			color = new Color(getSite().getShell().getDisplay(), BLACK);
		}
		item.setBackground(color);
	}
}
