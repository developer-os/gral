/**
 * GRAL: Vector export for Java(R) Graphics2D
 *
 * (C) Copyright 2009-2010 Erich Seifert <info[at]erichseifert.de>, Michael Seifert <michael.seifert[at]gmx.net>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.erichseifert.gral.plots.points;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.Format;
import java.text.NumberFormat;

import de.erichseifert.gral.plots.Label;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer2D;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.SettingChangeEvent;
import de.erichseifert.gral.util.Settings;
import de.erichseifert.gral.util.SettingsListener;


/**
 * Abstract class implementing functions for the administration of settings.
 */
public abstract class AbstractPointRenderer implements PointRenderer, SettingsListener {
	private final Settings settings;

	/**
	 * Creates a new AbstractPointRenderer object with default shape and
	 * color.
	 */
	public AbstractPointRenderer() {
		settings = new Settings(this);

		setSettingDefault(KEY_SHAPE, new Rectangle2D.Double(-2.5, -2.5, 5.0, 5.0));
		setSettingDefault(KEY_COLOR, Color.BLACK);

		setSettingDefault(KEY_VALUE_DISPLAYED, false);
		setSettingDefault(KEY_VALUE_FORMAT, NumberFormat.getInstance());
		setSettingDefault(KEY_VALUE_ALIGNMENT_X, 0.5);
		setSettingDefault(KEY_VALUE_ALIGNMENT_Y, 0.5);
		setSettingDefault(KEY_VALUE_COLOR, Color.BLACK);

		setSettingDefault(KEY_ERROR_DISPLAYED, false);
		setSettingDefault(KEY_ERROR_COLOR, Color.BLACK);
		setSettingDefault(KEY_ERROR_SHAPE, new Line2D.Double(-2.0, 0.0, 2.0, 0.0));
		setSettingDefault(KEY_ERROR_STROKE, new BasicStroke(1f));
	}

	/**
	 * Draws the specified value for the specified shape.
	 * @param g2d Graphics2D to be used for drawing.
	 * @param point Point to draw into.
	 * @param value Value to be displayed.
	 */
	protected void drawValue(Graphics2D g2d, Shape point, Object value) {
		Format format = getSetting(KEY_VALUE_FORMAT);
		String text = format.format(value);
		Label valueLabel = new Label(text);
		valueLabel.setSetting(Label.KEY_ALIGNMENT_X, getSetting(KEY_VALUE_ALIGNMENT_X));
		valueLabel.setSetting(Label.KEY_ALIGNMENT_Y, getSetting(KEY_VALUE_ALIGNMENT_Y));
		valueLabel.setSetting(Label.KEY_COLOR, getSetting(KEY_VALUE_COLOR));
		valueLabel.setBounds(point.getBounds2D());
		valueLabel.draw(g2d);
	}

	protected void drawError(Graphics2D g2d, Shape point, double value, double errorTop, double errorBot, Axis axis, AxisRenderer2D axisRenderer) {
		double posX = point.getBounds2D().getCenterX();
		double valueTop = value + errorTop;
		double valueBot = value - errorBot;
		double posY = axisRenderer.getPosition(axis, value, true, false).getY();
		double posYTop = axisRenderer.getPosition(axis, valueTop, true, false).getY() - posY;
		double posYBot = axisRenderer.getPosition(axis, valueBot, true, false).getY() - posY;
		Point2D pointTop = new Point2D.Double(posX, posYTop);
		Point2D pointBot = new Point2D.Double(posX, posYBot);
		Line2D errorBar = new Line2D.Double(pointTop, pointBot);

		// Draw the error bar
		Paint errorPaint = getSetting(KEY_ERROR_COLOR);
		Stroke errorStroke = getSetting(KEY_ERROR_STROKE);
		GraphicsUtils.drawPaintedShape(g2d, errorBar, errorPaint, null, errorStroke);

		// Draw the shapes at the end of the error bars
		Shape endShape = getSetting(KEY_ERROR_SHAPE);
		AffineTransform txOld = g2d.getTransform();
		g2d.translate(posX, posYTop);
		Stroke endShapeStroke = new BasicStroke(1f);
		GraphicsUtils.drawPaintedShape(g2d, endShape, errorPaint, null, endShapeStroke);
		g2d.setTransform(txOld);
		g2d.translate(posX, posYBot);
		GraphicsUtils.drawPaintedShape(g2d, endShape, errorPaint, null, endShapeStroke);
		g2d.setTransform(txOld);
	}

	@Override
	public <T> T getSetting(String key) {
		return settings.<T>get(key);
	}

	@Override
	public <T> void setSetting(String key, T value) {
		settings.<T>set(key, value);
	}

	@Override
	public <T> void removeSetting(String key) {
		settings.remove(key);
	}

	@Override
	public <T> void setSettingDefault(String key, T value) {
		settings.<T>setDefault(key, value);
	}

	@Override
	public <T> void removeSettingDefault(String key) {
		settings.removeDefault(key);
	}

	@Override
	public void settingChanged(SettingChangeEvent event) {
	}
}