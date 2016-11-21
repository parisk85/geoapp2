package com.parisk85;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.esri.map.JMap;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;

public class GeoApp {

	private Location location;

	private JFrame window;
	private JMap map;
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	private JTabbedPane leftPanel;
	private JPanel geocodePanel;
	private JPanel reversePanel;
	private JPanel rightPanel;
	private JScrollPane tableScrollPane;
	private JTable geoTable;
	private String[] columnNames = { "FID", "Address", "Lat", "Lng" };
	private Object[][] data = new Object[100][100];
	private JLabel addressLabel;
	private JTextField addressField;
	private JButton searchAddress;

	private JLabel latLabel;
	private JLabel lngLabel;
	private JTextField latField;
	private JTextField lngField;

	public GeoApp() {
		window = new JFrame("Geocoding Application");
		window.setMinimumSize(new Dimension(480, 320));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());

		leftPanel = new JTabbedPane();
		///// left panel//////////
		addressLabel = new JLabel("Address: ");
		addressField = new JTextField();
		addressField.setPreferredSize(new Dimension(200, 24));
		searchAddress = new JButton("Search");
		//////////////////////////
		latLabel = new JLabel("Lat: ");
		latField = new JTextField();
		latField.setPreferredSize(new Dimension(130, 24));
		latField.setEditable(false);
		lngLabel = new JLabel("Lng: ");
		lngField = new JTextField();
		lngField.setPreferredSize(new Dimension(130, 24));
		lngField.setEditable(false);
		//////////////////////////
		geocodePanel = new JPanel();
		geocodePanel.add(addressLabel);
		geocodePanel.add(addressField);
		geocodePanel.add(searchAddress);
		geocodePanel.add(latLabel);
		geocodePanel.add(latField);
		geocodePanel.add(lngLabel);
		geocodePanel.add(lngField);
		reversePanel = new JPanel();

		leftPanel.add("Geocode", geocodePanel);
		leftPanel.add("Reverse", reversePanel);
		leftPanel.setMinimumSize(new Dimension(350, 10));
		/////////////////////////
		rightPanel = new JPanel(new BorderLayout());
		geoTable = new JTable(data, columnNames);
		tableScrollPane = new JScrollPane(geoTable);

		MapOptions mapOptions = new MapOptions(MapType.STREETS, 38.0103, 23.6264, 13);
		map = new JMap(mapOptions);
		rightPanel.add(map, BorderLayout.CENTER);

		horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightPanel, tableScrollPane);
		horizontalSplitPane.setDividerLocation(Integer.MAX_VALUE);
		verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, horizontalSplitPane);
		verticalSplitPane.setDividerLocation(350);

		window.add(verticalSplitPane);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				super.windowClosing(windowEvent);
				map.dispose();
			}
		});

		searchAddress.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (addressField.getText() != null)
					location = Locator.geocode(addressField.getText());

				if (location != null) {
					latField.setText(Double.toString(location.getLat()));
					lngField.setText(Double.toString(location.getLng()));
					map.centerAt(location.getLat(), location.getLng());					
				}
			}
		});
	}

	/**
	 * Starting point of this application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					GeoApp application = new GeoApp();
					application.window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

