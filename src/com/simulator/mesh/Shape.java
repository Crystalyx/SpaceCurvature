package com.simulator.mesh;

import org.lwjgl.opengl.GL11;

import Math.Vec.Vec2;
import Utilities.Icon;
import Utilities.Tess;

public class Shape
{
	public Vec2[] points;

	public Shape(Vec2[] shape)
	{
		points = shape;
	}

	public Vec2 getCenter()
	{
		Vec2 sum = new Vec2(0, 0);
		for (int i = 0; i < points.length; i++)
		{
			sum.addChanged(points[i]);
		}
		return sum.div(points.length);
	}

	public void render(Tess b)
	{
		b.start(GL11.GL_QUADS);
		b.switchTexture(Icon.sqr.getTexture());
		for (int i = 0; i < points.length; i++)
		{
			b.addVertexWithUV(points[i], 0, 0);
			b.addVertexWithUV(points[(i + 1) % points.length], 1, 0);
		}

		b.stop();
	}

	public static Shape rect(int a, int b)
	{
		return new Shape(new Vec2[] { new Vec2(0, 0), new Vec2(a, 0), new Vec2(a, b), new Vec2(0, b) });
	}

	public boolean contains(Vec2 point, Vec2 pos)
	{
		boolean ret = true;
		Vec2 o = this.getCenter();
		for (int i = 0; i < points.length; i++)
		{
			Vec2 A = points[i].sub(o);
			Vec2 B = points[(i + 1) % points.length].sub(o);
			Vec2 AB = A.sub(B);
			Vec2 AdB = B.add(A).div(2).add(pos).sub(point);
			double pdp = AB.pseudoDotProduct(AdB);
			ret = ret && pdp > 0;
		}
		return ret;
	}

	public boolean contains(Vec2 tpos, Shape sh, Vec2 spos)
	{
		boolean ret = true;
		for (int i = 0; i < sh.points.length; i++)
		{
			ret = ret || this.contains(sh.points[i].sub(spos), tpos);
		}
		return ret;
	}
}
