package net.egork.chelper.ui;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import net.egork.chelper.util.FileCreator;
import net.egork.chelper.util.Provider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Egor Kulikov (egorku@yandex-team.ru)
 */
public class SelectOrCreateClass extends JPanel {
	private final ClassSelector classSelector;
	private final JButton create;
	private String defaultClassName;

	public SelectOrCreateClass(String initialValue, final Project project, final Provider<String> locationProvider, final FileCreator fileCreator) {
		this(initialValue, project, locationProvider, fileCreator, null);
	}

	public SelectOrCreateClass(String initialValue, final Project project, final Provider<String> locationProvider,
							   final FileCreator fileCreator, String defaultClassName) {
		super(new BorderLayout());
		this.defaultClassName = defaultClassName;
		classSelector = new ClassSelector(initialValue, project);
		create = new JButton("Create");
		create.setEnabled(fileCreator.isValid(initialValue));
		classSelector.addDocumentListener(new DocumentListener() {
			public void beforeDocumentChange(DocumentEvent event) {
			}

			public void documentChanged(DocumentEvent event) {
				create.setEnabled(fileCreator.isValid(classSelector.getText()));
			}
		});
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classSelector.setText(fileCreator.createFile(project, locationProvider.provide(), classSelector.getText()));
			}
		});
		add(classSelector, BorderLayout.CENTER);
		add(create, BorderLayout.EAST);
	}

	public String getText() {
		String res = classSelector.getText();
		if (res.isEmpty() && defaultClassName != null) {
			res = defaultClassName;
		}
		return res;
	}

	public void setText(String text) {
		classSelector.setText(text);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		classSelector.setEnabled(enabled);
		create.setEnabled(enabled);
	}
}
