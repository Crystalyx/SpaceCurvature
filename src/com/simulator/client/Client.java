package com.simulator.client;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.simulator.core.CoreInitializer;
import com.simulator.player.Player;

public class Client implements ApplicationListener
{
	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Environment environment;

	public Model model;
	public ModelInstance instance;

	public Player p;

	@Override
	public void create()
	{
		p = CoreInitializer.p;
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(p.pos.fx(), p.pos.fy(), p.pos.fz());
		cam.lookAt(p.look.fx(), p.look.fy(), p.look.fz());
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		ModelBuilder modelBuilder = new ModelBuilder();
		model = modelBuilder.createBox(2f, 2f, 2f, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)), Usage.Position | Usage.Normal);

		instance = new ModelInstance(model);
	}

	float a = 0;

	@Override
	public void render()
	{
		CoreInitializer.update();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		a += 1;
		// rotateRelative(a, 0, 1, 0, x, y, z);
		modelBatch.begin(cam);
		modelBatch.render(instance, environment);
		modelBatch.end();

		cam.rotate(new Vector3(0, 1, 0), 0.1f);
		cam.update();
	}
	
	

	@Override
	public void dispose()
	{
		modelBatch.dispose();
		model.dispose();
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	public void rotateRelative(float deg, float rx, float ry, float rz, float x, float y, float z)
	{
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef(deg, rx, ry, rz);
		GL11.glTranslatef(-x, -y, -z);
	}
}