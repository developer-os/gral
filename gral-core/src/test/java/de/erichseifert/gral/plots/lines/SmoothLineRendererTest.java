/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2011 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <michael.seifert[at]gmx.net>
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
package de.erichseifert.gral.plots.lines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.erichseifert.gral.Drawable;
import de.erichseifert.gral.DrawingContext;
import de.erichseifert.gral.plots.DataPoint;
import de.erichseifert.gral.util.PointND;

public class SmoothLineRendererTest {

	@Test
	public void testLine() {
		// Get line
		LineRenderer r = new SmoothLineRenderer2D();
		List<DataPoint> points = Arrays.asList(
			new DataPoint(new PointND<Double>(0.0, 0.0), null, null),
			new DataPoint(new PointND<Double>(1.0, 1.0), null, null)
		);

		BufferedImage image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
		DrawingContext context = new DrawingContext((Graphics2D) image.getGraphics());
		r.setSetting(SmoothLineRenderer2D.SMOOTHNESS, 0.5);
		Drawable line = r.getLine(points);
		assertNotNull(line);
		line.draw(context);
	}

	@Test
	public void testSettings() {
		// Get
		LineRenderer r = new SmoothLineRenderer2D();
		assertEquals(Color.BLACK, r.getSetting(LineRenderer.COLOR));
		// Set
		r.setSetting(LineRenderer.COLOR, Color.RED);
		assertEquals(Color.RED, r.getSetting(LineRenderer.COLOR));
		// Remove
		r.removeSetting(LineRenderer.COLOR);
		assertEquals(Color.BLACK, r.getSetting(LineRenderer.COLOR));
	}

}