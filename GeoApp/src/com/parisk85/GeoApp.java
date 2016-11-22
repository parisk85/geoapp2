package com.parisk85;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.esri.core.geometry.CoordinateConversion;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapOptions;
import com.esri.map.MapOptions.MapType;
import com.esri.map.MapOverlay;

public class GeoApp extends MapOverlay {

	private static int row_counter = 0;
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
	private DefaultTableModel model;
	private String[] columnNames = { "ID", "Address", "Country", "State", "City", "Postal Code", "Crime Type",
			"Latitude", "Longitude" };
	private Object[][] data = new Object[5000][columnNames.length];
	private JLabel addressLabel;
	private JTextField addressField;
	private JButton searchAddress;
	private JLabel countryLabel;
	private JTextField countryField;
	private JLabel stateLabel;
	private JTextField stateField;
	private JLabel cityLabel;
	private JTextField cityField;
	private JLabel postalCodeLabel;
	private JTextField postalCodeField;
	private String[] crimeTypes = { "Car Theft", "Burglary" };
	private JLabel crimeTypeLabel;
	private JLabel reverseCrimeTypeLabel;
	private JComboBox<String> crimeTypeBox = new JComboBox(crimeTypes);
	private JComboBox<String> reverseCrimeTypeBox = new JComboBox(crimeTypes);
	private JTextField streetField;

	private JLabel latLabel;
	private JLabel lngLabel;
	private JTextField latField;
	private JTextField lngField;
	private JButton addCrimeButton;
	private JButton reverseAddCrimeButton;

	private JTextField reverseAddressField;
	private JLabel reverseLatLabel;
	private JTextField reverseLatField;
	private JLabel reverseLngLabel;
	private JTextField reverseLngField;
	private JLabel reverseCountryLabel;
	private JTextField reverseCountryField;
	private JLabel reverseStateLabel;
	private JTextField reverseStateField;
	private JLabel reverseCityLabel;
	private JTextField reverseCityField;
	private JLabel reversePostalCodeLabel;
	private JTextField reversePostalCodeField;

	private GraphicsLayer graphicsLayer = new GraphicsLayer();
	private PictureMarkerSymbol symbol = new PictureMarkerSymbol(
			"http://static.arcgis.com/images/Symbols/Basic/RedShinyPin.png");

	public GeoApp() {
		window = new JFrame("Geocoding Application");
		window.setMinimumSize(new Dimension(480, 320));
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());

		leftPanel = new JTabbedPane();
		streetField = new JTextField();
		streetField.setPreferredSize(new Dimension(320, 24));
		streetField.setEditable(false);
		// /// left panel//////////
		addressLabel = new JLabel("Address: ");
		addressField = new JTextField();
		addressField.setPreferredSize(new Dimension(200, 24));
		searchAddress = new JButton("Search");
		// ////////////////////////
		latLabel = new JLabel("Lat: ");
		latField = new JTextField();
		latField.setPreferredSize(new Dimension(130, 24));
		latField.setEditable(false);
		lngLabel = new JLabel("Lng: ");
		lngField = new JTextField();
		lngField.setPreferredSize(new Dimension(130, 24));
		lngField.setEditable(false);
		// ////////////////////////
		countryLabel = new JLabel("Country: ");
		countryField = new JTextField();
		countryField.setPreferredSize(new Dimension(100, 24));
		countryField.setEditable(false);
		stateLabel = new JLabel("State: ");
		stateField = new JTextField();
		stateField.setPreferredSize(new Dimension(110, 24));
		stateField.setEditable(false);
		// ////////////////////////
		cityLabel = new JLabel("City: ");
		cityField = new JTextField();
		cityField.setPreferredSize(new Dimension(100, 24));
		cityField.setEditable(false);
		postalCodeLabel = new JLabel("Postal Code: ");
		postalCodeField = new JTextField();
		postalCodeField.setPreferredSize(new Dimension(100, 24));
		postalCodeField.setEditable(false);
		// /////////////////////////
		crimeTypeLabel = new JLabel("Crime Type: ");
		crimeTypeBox.setPreferredSize(new Dimension(180, 24));
		// /////////////////////////
		addCrimeButton = new JButton("Add Crime");
		// right panel///////////////
		reverseAddressField = new JTextField();
		reverseAddressField.setPreferredSize(new Dimension(320, 24));
		reverseAddressField.setEditable(false);
		reverseLatLabel = new JLabel("Lat: ");
		reverseLatField = new JTextField();
		reverseLatField.setPreferredSize(new Dimension(130, 24));
		reverseLatField.setEditable(false);
		reverseLngLabel = new JLabel("Lng: ");
		reverseLngField = new JTextField();
		reverseLngField.setPreferredSize(new Dimension(130, 24));
		reverseLngField.setEditable(false);
		reverseCountryLabel = new JLabel("Country: ");
		reverseCountryField = new JTextField();
		reverseCountryField.setPreferredSize(new Dimension(100, 24));
		reverseCountryField.setEditable(false);
		reverseStateLabel = new JLabel("State: ");
		reverseStateField = new JTextField();
		reverseStateField.setPreferredSize(new Dimension(110, 24));
		reverseStateField.setEditable(false);
		// ////////////////////////
		reverseCityLabel = new JLabel("City: ");
		reverseCityField = new JTextField();
		reverseCityField.setPreferredSize(new Dimension(100, 24));
		reverseCityField.setEditable(false);
		reversePostalCodeLabel = new JLabel("Postal Code: ");
		reversePostalCodeField = new JTextField();
		reversePostalCodeField.setPreferredSize(new Dimension(100, 24));
		reversePostalCodeField.setEditable(false);
		reverseCrimeTypeLabel = new JLabel("Crime Type: ");
		reverseCrimeTypeBox.setPreferredSize(new Dimension(180, 24));
		// /////////////////////////
		reverseAddCrimeButton = new JButton("Add Crime");
		////////////////////////////
		geocodePanel = new JPanel();
		geocodePanel.add(addressLabel);
		geocodePanel.add(addressField);
		geocodePanel.add(searchAddress);
		geocodePanel.add(streetField);
		geocodePanel.add(latLabel);
		geocodePanel.add(latField);
		geocodePanel.add(lngLabel);
		geocodePanel.add(lngField);
		geocodePanel.add(countryLabel);
		geocodePanel.add(countryField);
		geocodePanel.add(stateLabel);
		geocodePanel.add(stateField);
		geocodePanel.add(cityLabel);
		geocodePanel.add(cityField);
		geocodePanel.add(postalCodeLabel);
		geocodePanel.add(postalCodeField);
		geocodePanel.add(crimeTypeLabel);
		geocodePanel.add(crimeTypeBox);
		geocodePanel.add(addCrimeButton);
		reversePanel = new JPanel();
		reversePanel.add(reverseAddressField);
		reversePanel.add(reverseLatLabel);
		reversePanel.add(reverseLatField);
		reversePanel.add(reverseLngLabel);
		reversePanel.add(reverseLngField);
		reversePanel.add(reverseCountryLabel);
		reversePanel.add(reverseCountryField);
		reversePanel.add(reverseStateLabel);
		reversePanel.add(reverseStateField);
		reversePanel.add(reverseCityLabel);
		reversePanel.add(reverseCityField);
		reversePanel.add(reversePostalCodeLabel);
		reversePanel.add(reversePostalCodeField);
		reversePanel.add(reverseCrimeTypeLabel);
		reversePanel.add(reverseCrimeTypeBox);
		reversePanel.add(reverseAddCrimeButton);

		leftPanel.add("Geocode", geocodePanel);
		leftPanel.add("Reverse", reversePanel);
		leftPanel.setMinimumSize(new Dimension(350, 10));
		// ///////////////////////
		rightPanel = new JPanel(new BorderLayout());
		model = new DefaultTableModel();
		geoTable = new JTable(model);
		for (String s : columnNames)
			model.addColumn(s);
		tableScrollPane = new JScrollPane(geoTable);

		MapOptions mapOptions = new MapOptions(MapType.STREETS, 38.0103, 23.6264, 14);
		map = new JMap(mapOptions);
		map.getLayers().add(graphicsLayer);
		map.addMapOverlay(this);
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
					streetField.setText(location.getStreet());
					countryField.setText(location.getCountry());
					stateField.setText(location.getState());
					cityField.setText(location.getCity());
					postalCodeField.setText(location.getPostalCode());
					map.centerAt(location.getLat(), location.getLng());
					SpatialReference mapSR = map.getSpatialReference();
					Point point = GeometryEngine.project(location.getLng(), location.getLat(), mapSR);
					Graphic pointGraphic = new Graphic(point, symbol);
					graphicsLayer.removeAll();
					graphicsLayer.addGraphic(pointGraphic);
				}
			}
		});

		addCrimeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (location != null) {
					System.out.println("ID: " + createRandomId(data));
					System.out.println("Address: " + location.getStreet());
					System.out.println("Country: " + location.getCountry());
					System.out.println("State: " + location.getState());
					System.out.println("City: " + location.getCity());
					System.out.println("Postal Code: " + location.getPostalCode());
					System.out.println("Crime Type: " + crimeTypeBox.getItemAt(crimeTypeBox.getSelectedIndex()));
					System.out.println("Latitude: " + location.getLat());
					System.out.println("Longitude: " + location.getLng());

					ExcelRow row = new ExcelRow();
					row.setId(createRandomId(data));
					row.setAddress(location.getStreet());
					row.setCountry(location.getCountry());
					row.setState(location.getState());
					row.setCity(location.getCity());
					row.setPostalCode(location.getPostalCode());
					row.setCrimeType(crimeTypeBox.getItemAt(crimeTypeBox.getSelectedIndex()));
					row.setLat(location.getLat());
					row.setLng(location.getLng());
					model.addRow(new Object[] { row.getId(), row.getAddress(), row.getCountry(), row.getState(),
							row.getCity(), row.getPostalCode(), row.getCrimeType(), row.getLat(), row.getLng() });
				}
			}
		});

		reverseAddCrimeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (location != null) {
					System.out.println("ID: " + createRandomId(data));
					System.out.println("Address: " + location.getStreet());
					System.out.println("Country: " + location.getCountry());
					System.out.println("State: " + location.getState());
					System.out.println("City: " + location.getCity());
					System.out.println("Postal Code: " + location.getPostalCode());
					System.out.println("Crime Type: " + crimeTypeBox.getItemAt(crimeTypeBox.getSelectedIndex()));
					System.out.println("Latitude: " + location.getLat());
					System.out.println("Longitude: " + location.getLng());

					ExcelRow row = new ExcelRow();
					row.setId(createRandomId(data));
					row.setAddress(location.getStreet());
					row.setCountry(location.getCountry());
					row.setState(location.getState());
					row.setCity(location.getCity());
					row.setPostalCode(location.getPostalCode());
					row.setCrimeType(crimeTypeBox.getItemAt(crimeTypeBox.getSelectedIndex()));
					row.setLat(location.getLat());
					row.setLng(location.getLng());
					model.addRow(new Object[] { row.getId(), row.getAddress(), row.getCountry(), row.getState(),
							row.getCity(), row.getPostalCode(), row.getCrimeType(), row.getLat(), row.getLng() });
				}
			}
		});

	}

	private static String createRandomId(Object[][] ids) {
		Random rnd = new Random();
		int random = rnd.nextInt(5000) + 1;
		String formatted = String.format("%04d", random);
		return "ΔΣ" + formatted;
	}

	@Override
	public void onMouseClicked(MouseEvent event) {
		super.onMouseMoved(event);
		if (leftPanel.getSelectedIndex() == 1) {
			try {
				java.awt.Point screenPoint = event.getPoint();
				com.esri.core.geometry.Point mapPoint = map.toMapPoint(screenPoint.x, screenPoint.y);
				SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, Style.CROSS);

				SpatialReference spacRef = SpatialReference.create(4326);
				Point ltLn = (Point) GeometryEngine.project(mapPoint, map.getSpatialReference(), spacRef);
				DecimalFormat formatter = new DecimalFormat("##.######");

				Graphic pointGraphic = new Graphic(mapPoint, simpleMarker);
				graphicsLayer.removeAll();
				graphicsLayer.addGraphic(pointGraphic);
				location = Locator.reverse(ltLn);
				if (location != null) {
					reverseAddressField.setText(location.getStreet());
					reverseLatField.setText(Double.toString(location.getLat()));
					reverseLngField.setText(Double.toString(location.getLng()));
					reverseCountryField.setText(location.getCountry());
					reverseStateField.setText(location.getState());
					reverseCityField.setText(location.getCity());
					reversePostalCodeField.setText(location.getPostalCode());					
				}
			} catch (Exception e) {

			}
		}
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
