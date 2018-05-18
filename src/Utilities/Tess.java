package Utilities;

import java.lang.reflect.Field;
import java.util.Stack;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import Math.Vec.Vec2;

public class Tess extends SpriteBatch
{
	public static final int QUADS = 4;
	public static final int TRIANGLES = 3;
	public static final int LINES = 1;
	public static final int DOTS = 0;
	public static Tess instance;
	public static boolean drawing = false;
	/**
	 * 0 - dots 1 - lines 2 - 3 - triangles 4 - quads
	 */
	public static int type = 0;

	public static Stack<Matrix4> matrix = new Stack<Matrix4>();

	public Texture texture;

	public void pushMatrix()
	{
		matrix.push(this.getTransformMatrix().cpy());
		this.setTransformMatrix(this.getTransformMatrix());
	}

	public void resetMatrix()
	{
		this.setTransformMatrix(new Matrix4());
	}

	public void popMatrix()
	{
		this.setTransformMatrix(matrix.pop());
	}

	public void renderAABB(AABB2 ab)
	{
		begin();
		draw(texture, (float) ab.ox, (float) ab.oy - 1, (float) (ab.tx - ab.ox), 2);
		draw(texture, (float) ab.ox, (float) ab.ty - 1, (float) (ab.tx - ab.ox), 2);
		draw(texture, (float) ab.ox - 1, (float) ab.oy, 2, (float) (ab.ty - ab.oy));
		draw(texture, (float) ab.tx - 1, (float) ab.oy, 2, (float) (ab.ty - ab.oy));
		end();
	}

	public void renderAABB(Texture t, AABB2 ab)
	{
		t.bind();
		renderAABB(ab);
	}

	public void renderSqr(AABB2 ab)
	{
		begin();
		draw(texture, (float) ab.ox, (float) ab.oy, (float) (ab.tx - ab.ox), (float) (ab.ty - ab.oy));
		end();
	}

	public void renderSqr(Texture t, AABB2 ab)
	{
		t.bind();
		renderSqr(ab);
	}

	public void renderSqrWithUV(AABB2 ab, AABB2 uv)
	{
		begin();
		draw(texture, (float) ab.ox, (float) ab.oy, (float) ab.tx, (float) ab.ty, (float) uv.ox, (float) uv.oy, (float) uv.tx, (float) uv.ty);
		end();
	}

	public void renderSqrWithUV(Texture t, AABB2 ab, AABB2 uv)
	{
		t.bind();
		renderSqrWithUV(ab, uv);
	}

	public void start(int typ)
	{
		this.start(typ, this.texture);
	}

	public void start(int typ, Texture t)
	{
		if (!drawing)
		{
			type = typ;
			this.begin();

			drawing = true;

			float[] vertices = (float[]) getField("vertices");

			if (t != null)
			{
				t.bind();
				if (getField("lastTexture") == null)
					setField("lastTexture", t);
				if (t != getField("lastTexture"))
					switchTexture(t);
				else
					if (getField("idx") == (Integer) vertices.length) //
						flush();
			}
		}
	}

	public void start(int typ, Icon t)
	{
		this.start(typ, t.getTexture());
	}

	public void switchTexture(Texture texture)
	{
		setField("lastTexture", texture);
		setField("invTexWidth", 1.0f / texture.getWidth());
		setField("invTexHeight", 1.0f / texture.getHeight());
	}

	public void addVertexWithUV(float x, float y, float u, float v)
	{
		if (drawing)
		{
			float[] vertices = (float[]) getField("vertices");

			float color = (Float) getField("color");
			int idx = (Integer) getField("idx");
			vertices[idx] = x;
			vertices[idx + 1] = y;
			vertices[idx + 2] = color;
			vertices[idx + 3] = u;
			vertices[idx + 4] = v;
			setField("idx", idx + 5);
		}
		else
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
	}

	public void addVertexWithUV(Vec2 p, float u, float v)
	{
		this.addVertexWithUV((float) p.x, (float) p.y, u, v);
	}

	public void addVertexWithUV(double x, double y, double u, double v)
	{
		this.addVertexWithUV((float) x, (float) y, (float) u, (float) v);
	}

	public void stop()
	{
		if (drawing)
		{
			this.end();
			drawing = false;
		}
	}

	public void rotate(float deg, float x, float y, float z)
	{
		Matrix4 m = this.getTransformMatrix();
		Matrix4 t = m.mul(new Matrix4().rotate(x, y, z, deg));
		this.setTransformMatrix(t);
	}

	public void rotateRelative(float deg, float rx, float ry, float rz, float x, float y, float z)
	{
		translate(x, y, z);
		rotate(deg, rx, ry, rz);
		translate(-x, -y, -z);
	}

	public void translate(float x, float y, float z)
	{
		Matrix4 m = this.getTransformMatrix();
		Matrix4 t = m.mul(new Matrix4().translate(x, y, z));
		this.setTransformMatrix(t);
	}

	public void scale(float x, float y, float z)
	{
		Matrix4 m = this.getTransformMatrix();
		Matrix4 t = m.mul(new Matrix4().scale(x, y, z));
		this.setTransformMatrix(t);
	}

	public Object getField(String name)
	{
		try
		{
			Class clazz = Class.forName("com.badlogic.gdx.graphics.g2d.SpriteBatch");
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return f.get(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void setField(String name, Object val)
	{
		try
		{
			Class clazz = Class.forName("com.badlogic.gdx.graphics.g2d.SpriteBatch");
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			f.set(this, val);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void drawTexturedModalRect(double x, double y, double u, double v, double w, double h, double uw, double vh, Icon icon)
	{
		if (!drawing)
			throw new IllegalStateException("SpriteBatch.begin must be called before draw.");

		float[] vertices = (float[]) getField("vertices");

		if (icon.getTexture() != getField("lastTexture"))
			switchTexture(icon.getTexture());
		else
			if (getField("idx") == (Integer) vertices.length) //
				flush();

		final float fx2 = (float) (x + w);
		final float fy2 = (float) (y + h);
		final float uwt = (float) (u + uw) / icon.width();
		final float vht = (float) (v + vh) / icon.height();
		final float ut = (float) (u + uw) / icon.width();
		final float vt = (float) (v + vh) / icon.height();

		float color = (Float) getField("color");
		int idx = (Integer) getField("idx");
		vertices[idx] = (float) x;
		vertices[idx + 1] = (float) y;
		vertices[idx + 2] = color;
		vertices[idx + 3] = (float) ut;
		vertices[idx + 4] = (float) vt;

		vertices[idx + 5] = (float) x;
		vertices[idx + 6] = fy2;
		vertices[idx + 7] = color;
		vertices[idx + 8] = ut;
		vertices[idx + 9] = vht;

		vertices[idx + 10] = fx2;
		vertices[idx + 11] = fy2;
		vertices[idx + 12] = color;
		vertices[idx + 13] = uwt;
		vertices[idx + 14] = vht;

		vertices[idx + 15] = fx2;
		vertices[idx + 16] = (float) y;
		vertices[idx + 17] = color;
		vertices[idx + 18] = uwt;
		vertices[idx + 19] = vht;
		setField("idx", idx + 20);
	}

}
