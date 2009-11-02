package openjchart.plots;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.Border;

import openjchart.Drawable;
import openjchart.data.DataListener;
import openjchart.data.DataSource;
import openjchart.plots.axes.Axis;
import openjchart.util.Settings;
import openjchart.util.SettingsStorage;

public abstract class Plot extends JPanel implements SettingsStorage, DataListener {
	public static final String KEY_TITLE = "plot.title";
	public static final String KEY_BACKGROUND_COLOR = "plot.background.color";
	public static final String KEY_ANTIALISING = "plot.antialiasing";

	private final Settings settings;

	private final Map<String, Axis> axes;
	private final Map<String, Drawable> axisDrawables;

	private Label title;

	public Plot() {
		this.axes = new HashMap<String, Axis>();
		this.axisDrawables = new HashMap<String, Drawable>();
		this.settings = new Settings();
		this.title = new Label("");
		this.title.setSetting(Label.KEY_FONT, new Font("Arial", Font.BOLD, 18));
		setSettingDefault(KEY_TITLE, null);
		setSettingDefault(KEY_BACKGROUND_COLOR, Color.WHITE);
		setSettingDefault(KEY_ANTIALISING, true);
		setBackground(this.<Color>getSetting(KEY_BACKGROUND_COLOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				this.<Boolean>getSetting(KEY_ANTIALISING) ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		AffineTransform txOld = g2d.getTransform();

		if (title != null) {
			title.draw(g2d);
		}

		// Draw axes
		for (Drawable axis : axisDrawables.values()) {
			g2d.translate(axis.getX(), axis.getY());
			axis.draw(g2d);
			g2d.setTransform(txOld);
		}
	}

	@Override
	public Insets getInsets() {
		Border border = getBorder();
		if (border != null) {
			return border.getBorderInsets(this);
		}

		return new Insets(0, 0, 0, 0);
	}

	public Axis getAxis(String name) {
		return axes.get(name);
	}

	public void setAxis(String name, Axis axis) {
		if (axis == null) {
			removeAxis(name);
		}
		axes.put(name, axis);
		axisDrawables.put(name, null);
	}

	public void setAxis(String name, Axis axis, Drawable drawable) {
		if (axis == null) {
			removeAxis(name);
		}
		axes.put(name, axis);
		axisDrawables.put(name, drawable);
	}

	public void removeAxis(String name) {
		axes.remove(name);
	}

	@Override
	public <T> T getSetting(String key) {
		return settings.<T>get(key);
	}

	@Override
	public <T> void setSetting(String key, T value) {
		settings.<T>set(key, value);
		if (KEY_TITLE.equals(key) && value != null) {
			title.setText((String) value);
		}
	}

	@Override
	public <T> void setSettingDefault(String key, T value) {
		settings.set(key, value);
		if (KEY_TITLE.equals(key) && value != null) {
			title.setText((String) value);
		}
	}

	@Override
	public void dataChanged(DataSource data) {
	}

	public Label getTitle() {
		return title;
	}
}