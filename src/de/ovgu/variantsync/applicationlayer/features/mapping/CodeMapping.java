package de.ovgu.variantsync.applicationlayer.features.mapping;

import java.util.ArrayList;
import java.util.List;

import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeFragment;
import de.ovgu.variantsync.applicationlayer.datamodel.features.CodeLine;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaClass;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaElement;
import de.ovgu.variantsync.applicationlayer.datamodel.features.JavaPackage;
import de.ovgu.variantsync.presentationlayer.controller.data.MappingElement;

public class CodeMapping extends Mapping {

	private IMappingOperations classMapping;
	private IMappingOperations packageMapping;

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
				try {
					tmpCode.add(cl.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			List<CodeLine> newLines = UtilOperations.getInstance().addCode(
					new CodeFragment(code, mapping.getStartLineOfSelection(),
							mapping.getEndLineOfSelection(),
							mapping.getOffset()), actualCode);
			javaElement.setCodeLines(newLines);
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
			CodeFragment code) {
			String nameOfClass = javaElement.getName();
			String pathOfClass = UtilOperations.getInstance().removeSrcInPath(
					javaElement.getPath());
			if (nameOfClass.equals(elementName)
					&& pathOfClass.equals(UtilOperations.getInstance()
							.removeSrcInPath(elementPath))) {
				List<CodeLine> tmpCode = new ArrayList<CodeLine>();
				List<CodeLine> actualCode = javaElement.getClonedCodeLines();
				for (CodeLine cl : actualCode) {
					try {
						tmpCode.add(cl.clone());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
				return javaElement.setCodeLines(UtilOperations.getInstance()
						.removeCode(code.getStartLine(), code.getEndLine(),
								tmpCode));
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
