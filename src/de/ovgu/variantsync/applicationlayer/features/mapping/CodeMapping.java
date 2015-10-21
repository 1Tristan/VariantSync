package de.ovgu.variantsync.applicationlayer.features.mapping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.ovgu.variantsync.VariantSyncPlugin;
import de.ovgu.variantsync.applicationlayer.ModuleFactory;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeFragment;
import de.ovgu.variantsync.applicationlayer.datamodel.context.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.context.JavaPackage;
import de.ovgu.variantsync.applicationlayer.datamodel.exception.FileOperationException;
import de.ovgu.variantsync.persistencelayer.IPersistanceOperations;
import de.ovgu.variantsync.presentationlayer.controller.data.MappingElement;

public class CodeMapping extends Mapping {

	private IMappingOperations classMapping;
	private IMappingOperations packageMapping;
	private IPersistanceOperations persistanceOperations = ModuleFactory
			.getPersistanceOperations();

	public CodeMapping() {
		classMapping = new ClassMapping();
		packageMapping = new PackageMapping();
	}

	@Override
	public JavaElement getElement(List<JavaElement> javaPackages,
			String className, String classPath) {
		return null;
	}

	@Override
	public JavaElement createElement(String pathToProject, String elementName,
			String pathToElement) {
		return null;
	}

	@Override
	public boolean containsElement(List<JavaElement> elements,
			String elementName, String pathToElement, String contentOfElement) {
		return false;
	}

	@Override
	protected void computeElement(JavaElement element, MappingElement mapping,
			String name, String path) {
		List<String> code = mapping.getCode();
		String relativeClassPath = UtilOperations.getInstance()
				.getRelativeClassPath(path);
		if (classMapping.containsElement(element.getChildren(), name, path, "")) {
			JavaElement javaElement = classMapping.getElement(
					element.getChildren(), name, relativeClassPath);
			List<CodeLine> tmpCode = new ArrayList<CodeLine>();
			List<CodeLine> actualCode = javaElement.getClonedCodeLines();
			for (CodeLine cl : actualCode) {
				tmpCode.add(cl.clone());
			}
			List<CodeLine> newLines = UtilOperations.getInstance().addCode(
					new CodeFragment(code, mapping.getStartLineOfSelection(),
							mapping.getEndLineOfSelection(),
							mapping.getOffset()), actualCode);
			
			// TODO: read base version before save!!!
			// TODO: String to List<CodeLine> after JDime merge
			if (mapping.isFirstStep() || actualCode.isEmpty()) {
				if (((JavaClass) javaElement).getCodeLines().isEmpty()) {
					((JavaClass) javaElement)
							.setWholeClass(getCodeLinesFromFile(
									element.getName(), name));
				}
				((JavaClass) javaElement).setBaseVersion();
			}
			javaElement.setCodeLines(newLines);
			((JavaClass) javaElement).setWholeClass(mapping.getWholeClass());
			if (mapping.isLastStep()) {
				((JavaClass) javaElement).addChange(newLines);
			}
		} else {
			JavaElement packageOfClass = packageMapping.getElement(
					element.getChildren(), name, relativeClassPath);
			if (packageOfClass == null) {
				String packageName = UtilOperations.getInstance()
						.parsePackageName(path);
				path = UtilOperations.getInstance().parsePackagePath(path);
				packageOfClass = new JavaPackage(packageName, path);
				element.addChild(packageOfClass);
			}
			packageOfClass.addChild(new JavaClass(name, path + "/" + name,
					new CodeFragment(code, mapping.getStartLineOfSelection(),
							mapping.getEndLineOfSelection(), mapping
									.getOffset())));
		}
	}

	private List<String> getCodeLinesFromFile(String projectName,
			String className) {
		List<String> linesOfFile = null;
		List<IProject> supportedProjects = VariantSyncPlugin.getDefault()
				.getSupportProjectList();
		for (IProject p : supportedProjects) {
			String name = p.getName();
			if (name.equals(projectName)) {
				IResource javaClass = null;
				try {
					javaClass = findFileRecursively(p, className);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if (javaClass != null) {
					IFile file = (IFile) javaClass;
					try {
						linesOfFile = persistanceOperations.readFile(
								file.getContents(), file.getCharset());
						break;
					} catch (FileOperationException | CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return linesOfFile;
	}

	private IFile findFileRecursively(IContainer container, String name)
			throws CoreException {
		for (IResource r : container.members()) {
			if (r instanceof IContainer) {
				IFile file = findFileRecursively((IContainer) r, name);
				if (file != null) {
					return file;
				}
			} else if (r instanceof IFile && r.getName().equals(name)) {
				return (IFile) r;
			}
		}

		return null;
	}

	@Override
	protected JavaElement createProject(String pathToProject,
			String elementName, String elementPath, MappingElement mapping) {
		String relativeClassPath = UtilOperations.getInstance()
				.getRelativeClassPath(elementPath);
		return ((ClassMapping) classMapping).addClassWithCode(UtilOperations
				.getInstance().parseProjectPath(elementPath), elementName,
				relativeClassPath, mapping.getCode(), mapping
						.getStartLineOfSelection(), mapping
						.getEndLineOfSelection(), mapping.getOffset());
	}

	@Override
	protected boolean removeElement(JavaElement javaElement,
			List<JavaElement> elements, String elementName, String elementPath,
			CodeFragment code, boolean isFirstStep, boolean isLastStep,
			List<String> wholeClass) {
		String nameOfClass = javaElement.getName();
		String pathOfClass = UtilOperations.getInstance().removeSrcInPath(
				javaElement.getPath());
		if (nameOfClass.equals(elementName)
				&& pathOfClass.equals(UtilOperations.getInstance()
						.removeSrcInPath(elementPath))) {
			List<CodeLine> tmpCode = new ArrayList<CodeLine>();
			List<CodeLine> actualCode = javaElement.getClonedCodeLines();
			for (CodeLine cl : actualCode) {
				tmpCode.add(cl.clone());
			}
			((JavaClass) javaElement).setWholeClass(wholeClass);
			return javaElement
					.setCodeLines(UtilOperations.getInstance().removeCode(
							code.getStartLine(), code.getEndLine(), tmpCode));
		}
		return false;
	}

	@Override
	protected boolean checkElement(JavaElement element, String elementName,
			String pathToElement) {
		return false;
	}

	@Override
	protected JavaElement computeElement(JavaElement element, String name,
			String path) {
		return null;
	}

}
