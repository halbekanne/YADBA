/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Material;
import view.util.TextFieldWithWarning;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controller.OfficeController;

public class MaterialFormular extends JDialog {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	private final OfficeController officeCtrl;

	/** das Material das bearbeitet wird oder null */
	private final Material material;

	// die Komponenten
	private final JPanel contentPanel = new JPanel();
	private TextFieldWithWarning textName;
	private final JSpinner spinnerPrice;

	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public MaterialFormular(OfficeController officeController) {
		this(officeController, null);
	}

	/**
	 * Create the dialog.
	 */
	public MaterialFormular(OfficeController officeController, Material material) {
		this.officeCtrl = officeController;
		this.material = material;

		setTitle("Neues Material eintragen");
		setBounds(100, 100, 297, 184);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));
		{
			JLabel lblBezeichnung = new JLabel("Bezeichnung:");
			lblBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(lblBezeichnung, "2, 2, left, top");
		}
		{
			textName = new TextFieldWithWarning("", false);
			textName.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					okButton.setEnabled(textName.hasValidInput());
				}
			});
			contentPanel.add(textName, "4, 2, fill, top");
			textName.setColumns(10);
		}
		{
			JLabel lblPreis = new JLabel("Preis (€):");
			contentPanel.add(lblPreis, "2, 4, left, top");
		}
		{
			spinnerPrice = new JSpinner();
			spinnerPrice.setModel(new SpinnerNumberModel(1f, 0f, null, .01f));
			DecimalFormat format = ((JSpinner.NumberEditor) spinnerPrice
					.getEditor()).getFormat();
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(2);
			contentPanel.add(spinnerPrice, "4, 4, fill, top");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Speichern");
				okButton.setEnabled(false);
				okButton.setMnemonic('s');
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (saveInput()) {
							dispose();
						} else {
							JOptionPane.showMessageDialog(
									MaterialFormular.this,
									"Konnte nicht erfolgreich speichern!",
									"Fehlern bei Speichern",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.setMnemonic('a');
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}

		fillComponents();
	}

	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 * */
	private void fillComponents() {
		String name;
		int price;

		if (material == null) {
			name = "";
			price = 1;
		} else {
			name = material.getName();
			price = material.getPrice();
		}

		textName.setText(name);
		spinnerPrice.setValue(price);
	}

	private boolean saveInput() {
		int price = (int) (((SpinnerNumberModel) spinnerPrice.getModel())
				.getNumber().floatValue() * 100);

		Material newMaterial = new Material(textName.getText(), price);

		if (material == null) {
			return officeCtrl.getServiceController().saveService(newMaterial);
		} else {
			// Materialien werden nicht bearbeitet
		}

		return true;
	}

	@Override
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

}
